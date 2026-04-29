package com.example.photobasedtextrpg.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldSettingDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(worldSetting: WorldSetting)

  @Query("SELECT * FROM world_setting WHERE id = :id")
  suspend fun findById(id: String): WorldSetting?

  @Query("SELECT * FROM world_setting ORDER BY createdAt DESC")
  fun getAll(): Flow<List<WorldSetting>>
}