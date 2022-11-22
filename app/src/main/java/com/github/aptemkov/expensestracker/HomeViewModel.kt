package com.github.aptemkov.expensestracker

import androidx.lifecycle.*
import com.github.aptemkov.expensestracker.domain.Item
import com.github.aptemkov.expensestracker.domain.ItemDao
import java.text.NumberFormat


class HomeViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    fun getTotalExpense(list: List<Item>): Double {
        return list.sumOf { expense -> expense.itemPrice }
    }

    fun getTotalCouldSave(list: List<Item>): Double {
        return list
            .filter { !it.isCompulsory }
            .sumOf { expense -> expense.itemPrice }
    }

    fun getFormattedWithCurrencyValue(value: Double): String {
        return NumberFormat.getCurrencyInstance().format(value)
    }

    fun getMapForPieChart(list: List<Item>): Map<String, Double> {
        val map = mutableMapOf<String, Double>()

        for(expense in list) {
            map[expense.itemCategory] =
                map.getOrDefault(expense.itemCategory, 0.0) + expense.itemPrice
        }

        return map.toMap()
    }
}


class HomeViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}