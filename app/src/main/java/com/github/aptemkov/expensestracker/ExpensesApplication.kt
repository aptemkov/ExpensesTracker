package com.github.aptemkov.expensestracker

import android.app.Application
import com.github.aptemkov.expensestracker.domain.transaction.TransactionRoomDatabase

class ExpensesApplication : Application() {
    val database: TransactionRoomDatabase by lazy { TransactionRoomDatabase.getDatabase(this)}
}
