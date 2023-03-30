package com.github.aptemkov.expensestracker.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.aptemkov.expensestracker.data.transaction.TransactionDao
import com.github.aptemkov.expensestracker.presentation.list.TransactionsViewModel

class ViewModelFactory(
    private val app: Application
): ViewModelProvider.Factory {
/* TODO()
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = when(modelClass) {
                TransactionsViewModel::class.java -> {
                    TransactionsViewModel(transactionDao = TransactionDao)
                }
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }*/
}