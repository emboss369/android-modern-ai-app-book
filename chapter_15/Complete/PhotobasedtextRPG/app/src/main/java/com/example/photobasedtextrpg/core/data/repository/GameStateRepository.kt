package com.example.photobasedtextrpg.core.data.repository

import com.example.photobasedtextrpg.core.data.db.AdventureHistoryEntity
import com.example.photobasedtextrpg.core.data.db.GameState
import com.example.photobasedtextrpg.core.data.db.GameStateDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameStateRepository(private val dao: GameStateDao) {

  suspend fun save(gameState: GameState): Result<Unit> = runCatching {
    dao.insert(gameState)
  }

  suspend fun update(gameState: GameState): Result<Unit> = runCatching {
    dao.update(gameState)
  }

  suspend fun findById(id: String): Result<GameState?> = runCatching {
    dao.findById(id)
  }

  fun getCompletedAdventures(): Flow<Result<List<AdventureHistoryEntity>>> =
    dao.getCompletedAdventures().map { Result.success(it) }
}