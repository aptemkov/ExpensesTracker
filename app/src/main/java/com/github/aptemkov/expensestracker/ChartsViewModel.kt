package com.github.aptemkov.expensestracker

import androidx.lifecycle.*
import com.github.aptemkov.expensestracker.domain.transaction.Transaction
import com.github.aptemkov.expensestracker.domain.transaction.Transaction.Companion.EXPENSE
import com.github.aptemkov.expensestracker.domain.transaction.Transaction.Companion.INCOME
import com.github.aptemkov.expensestracker.domain.transaction.TransactionDao
import java.text.NumberFormat


class ChartsViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    val allItems: LiveData<List<Transaction>> = transactionDao.getItems().asLiveData()

    fun getTotalBalance(list: List<Transaction>): Double {
        return getTotalIncome(list) - getTotalExpense(list)
    }

    fun getTotalExpense(list: List<Transaction>): Double {
        return list
            .filter { it.transactionType == EXPENSE }
            .sumOf { expense -> expense.transactionPrice }
    }

    fun getTotalIncome(list: List<Transaction>): Double {
        return list
            .filter { it.transactionType == INCOME }
            .sumOf { expense -> expense.transactionPrice }
    }

    fun getTotalCouldSave(list: List<Transaction>): Double {
        return list
            .filter { it.transactionType == EXPENSE }
            .filter { !it.isCompulsory }
            .sumOf { expense -> expense.transactionPrice }
    }

    fun getFormattedWithCurrencyValue(value: Double): String {
        return NumberFormat.getCurrencyInstance().format(value)
    }

    fun getMapForCubicChart(list: List<Transaction>): List<Pair<Float, Float>> {
        val pair = mutableListOf<Pair<Float, Float>>()
        for((index, transaction) in list.withIndex()) {
            pair.add(Pair(index * 100f, transaction.transactionPrice.toFloat()))
        }
        return pair.toList()
    }

    fun getMapForPieChart(list: List<Transaction>): Map<String, Double> {
        val map = mutableMapOf<String, Double>()

        for (transaction in list.filter { it.transactionType == EXPENSE }) {
            map[transaction.transactionCategory] =
                map.getOrDefault(transaction.transactionCategory, 0.0) + transaction.transactionPrice
        }
        return map.toMap()
    }
}


class HomeViewModelFactory(private val transactionDao: TransactionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel>                             create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChartsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChartsViewModel(transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}