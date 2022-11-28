package com.github.aptemkov.expensestracker.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.DeleteTable
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("DELETE FROM item")
    suspend fun deleteAll()

    @Query("SELECT * FROM item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * FROM item WHERE transactionType = :type ORDER BY date DESC")
    fun getItemsByType(type: String): Flow<List<Item>>

    @Query("SELECT * FROM item ORDER BY date DESC")
    fun getItems(): Flow<List<Item>>
}