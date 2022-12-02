package com.github.aptemkov.expensestracker.domain.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity(tableName = "transaction" )
data class Transaction (
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    @ColumnInfo(name = "transactionType")
    var transactionType: String = EXPENSE,
    @ColumnInfo(name = "category")
    val itemCategory: String,
    @ColumnInfo(name = "price")
    val itemPrice: Double,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "compulsory")
    val isCompulsory: Boolean = true,
) {
    companion object {
        const val EXPENSE = "expense"
        const val INCOME = "income"
        const val ALL_TRANSACTIONS = "all"
    }
}



fun Transaction.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(itemPrice)