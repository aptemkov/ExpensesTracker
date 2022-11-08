package com.github.aptemkov.expensestracker

import android.app.Application
import com.github.aptemkov.expensestracker.data.ItemRoomDatabase

class ExpensesApplication : Application() {
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this)}
}
