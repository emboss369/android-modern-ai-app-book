package com.example.photobasedtextrpg.core.domain.usecase

import com.example.photobasedtextrpg.core.data.db.GameState
import com.example.photobasedtextrpg.core.data.repository.GameStateRepository

class UpdateGameStateUseCase(private val repository: GameStateRepository) {
  suspend operator fun invoke(gameState: GameState): Result<Unit> =
    repository.update(gameState)
}