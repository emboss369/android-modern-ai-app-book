package com.example.mytodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
  @Query("SELECT * FROM items ORDER BY id ASC")
  fun getAllItems(): Flow<List<Item>>

  @Query("SELECT * FROM items WHERE id = :id")
  fun getItem(id: Int): Flow<Item?>

  @Insert
  suspend fun insert(item: Item)

  @Update
  suspend fun update(item: Item)

  @Delete
  suspend fun delete(item: Item)
}