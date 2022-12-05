package com.github.aptemkov.expensestracker

import androidx.lifecycle.*
import com.github.aptemkov.expensestracker.domain.transaction.Transaction
import com.github.aptemkov.expensestracker.domain.transaction.Transaction.Companion.EXPENSE
import com.github.aptemkov.expensestracker.domain.transaction.Transaction.Companion.INCOME
import com.github.aptemkov.expensestracker.domain.transaction.TransactionDao
import java.text.NumberFormat


class HomeViewModel(private val transactionDao: TransactionDao) : ViewModel() {

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

    fun getMapForPieChart(list: List<Transaction>): Map<String, Double> {
        val map = mutableMapOf<String, Double>()

        for (expense in list.filter { it.transactionType == EXPENSE }) {
            map[expense.transactionCategory] =
                map.getOrDefault(expense.transactionCategory, 0.0) + expense.transactionPrice
        }
        return map.toMap()
    }
}


class HomeViewModelFactory(private val transactionDao: TransactionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}