package com.example.photobasedtextrpg.core.domain.usecase

import com.example.photobasedtextrpg.core.data.repository.GameStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.map
import com.example.photobasedtextrpg.core.domain.model.AdventureHistory

class GetCompletedAdventuresUseCase(private val repository: GameStateRepository) {
  operator fun invoke(): Flow<Result<List<AdventureHistory>>> =
    repository.getCompletedAdventures().map { result ->
      result.map { entities ->
        entities.map { entity ->       // ❶ EntityをドメインモデルにMAP
          AdventureHistory(
            gameStateId = entity.gameState.id,
            worldSetting = entity.worldSetting,
            clearedAt = entity.gameState.createdAt
          )
        }
      }
    }
}