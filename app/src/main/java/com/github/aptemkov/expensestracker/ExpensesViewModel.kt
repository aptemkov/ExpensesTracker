package com.github.aptemkov.expensestracker

import androidx.lifecycle.*
import com.github.aptemkov.expensestracker.domain.transaction.Transaction
import com.github.aptemkov.expensestracker.domain.transaction.Transaction.Companion.EXPENSE
import com.github.aptemkov.expensestracker.domain.transaction.Transaction.Companion.INCOME
import com.github.aptemkov.expensestracker.domain.transaction.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ExpensesViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    var allTransactions: LiveData<List<Transaction>> = transactionDao.getItems().asLiveData()
    var allIncomes: LiveData<List<Transaction>> = transactionDao.getItemsByType(INCOME).asLiveData()
    var allExpenses: LiveData<List<Transaction>> = transactionDao.getItemsByType(EXPENSE).asLiveData()

    private fun insertItem(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.insert(transaction)
        }
    }

    private fun getNewItemEntry(
        itemTransactionType: String,
        itemName: String,
        itemPrice: String,
        isCompulsory: String,
        date: String
    ): Transaction {
        return Transaction(
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

    fun isEntryValid(itemCategory: String, itemPrice: String, date: String): Boolean {
        if (itemCategory.isBlank() || itemPrice.isBlank() || date.isBlank()
            || itemPrice.length > 9) {
            return false
        }
        return true
    }

    fun retrieveItem(id: Int): LiveData<Transaction> {
        return transactionDao.getItem(id).asLiveData()
    }

    fun getByType(type: String): Flow<List<Transaction>> {
        return transactionDao.getItemsByType(type)
    }

    private fun updateItem(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.update(transaction)
        }
    }

    fun deleteItem(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.delete(transaction)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            transactionDao.deleteAll()
        }
    }

    private fun getUpdatedItemEntry(
        itemId: Int,
        itemTransactionType: String,
        itemCategory: String,
        itemPrice: String,
        isCompulsory: String,
        date: String,
    ): Transaction {
        return Transaction(
            id = itemId,
            transactionType = itemTransactionType,
            itemCategory = itemCategory,
            itemPrice = itemPrice.toDouble(),
            isCompulsory = isCompulsory.toBoolean(),
            date = date.toLong()
        )
    }

    fun updateItem(
        itemId: Int,
        transactionType: String,
        itemCategory: String,
        itemPrice: String,
        itemCount: String,
        date: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, transactionType, itemCategory, itemPrice, itemCount, date)
        updateItem(updatedItem)
    }

    fun replaceList(type: String): LiveData<List<Transaction>> {
        return when(type) {
            INCOME -> allIncomes
            EXPENSE -> allExpenses
            else -> allTransactions
        }
    }
}


class ExpensesViewModelFactory(private val transactionDao: TransactionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}