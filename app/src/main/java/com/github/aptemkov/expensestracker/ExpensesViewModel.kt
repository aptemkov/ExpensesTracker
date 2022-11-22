package com.github.aptemkov.expensestracker

import androidx.lifecycle.*
import com.github.aptemkov.expensestracker.domain.Item
import com.github.aptemkov.expensestracker.domain.ItemDao
import kotlinx.coroutines.launch


class ExpensesViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()


    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun getNewItemEntry(
        itemTransactionType: String,
        itemName: String,
        itemPrice: String,
        isCompulsory: String,
        date: String
    ): Item {
        return Item(
            transactionType = itemTransactionType,
            itemCategory = itemName,
            itemPrice = itemPrice.toDouble(),
            isCompulsory = isCompulsory.toBoolean(),
            date = date.toLong()
        )
    }

    fun addNewItem(itemTransactionType: String, itemName: String, itemPrice: String, itemCount: String, date: String) {
        val item = getNewItemEntry(itemTransactionType, itemName, itemPrice, itemCount, date)
        insertItem(item)
    }

    fun isEntryValid(itemName: String, itemPrice: String, date: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || date.isBlank()
            || itemPrice.length > 9) {
            return false
        }
        return true
    }

    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    private fun getUpdatedItemEntry(
        itemId: Int,
        itemTransactionType: String,
        itemName: String,
        itemPrice: String,
        isCompulsory: String,
        date: String,
    ): Item {
        return Item(
            id = itemId,
            transactionType = itemTransactionType,
            itemCategory = itemName,
            itemPrice = itemPrice.toDouble(),
            isCompulsory = isCompulsory.toBoolean(),
            date = date.toLong()
        )
    }

    fun updateItem(
        itemId: Int,
        transactionType: String,
        itemName: String,
        itemPrice: String,
        itemCount: String,
        date: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, transactionType, itemName, itemPrice, itemCount, date)
        updateItem(updatedItem)
    }



}


class ExpensesViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}