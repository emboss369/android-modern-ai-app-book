package com.example.photobasedtextrpg.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameStateDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(gameState: GameState)

  @Update
  suspend fun update(gameState: GameState)

  @Query("SELECT * FROM game_state WHERE id = :id")
  suspend fun findById(id: String): GameState?

  @Transaction   // ❶ JOINを含むクエリはこのアノテーションが必要
  @Query("SELECT * FROM game_state WHERE currentTurn = 5 ORDER BY createdAt DESC")
  fun getCompletedAdventures(): Flow<List<AdventureHistoryEntity>>
}