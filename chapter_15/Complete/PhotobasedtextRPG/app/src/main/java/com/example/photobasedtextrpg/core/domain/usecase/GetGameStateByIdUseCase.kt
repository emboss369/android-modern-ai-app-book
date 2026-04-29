package com.example.photobasedtextrpg.core.domain.usecase

import com.example.photobasedtextrpg.core.data.db.GameState
import com.example.photobasedtextrpg.core.data.repository.GameStateRepository

class GetGameStateByIdUseCase(private val repository: GameStateRepository) {
  suspend operator fun invoke(id: String): Result<GameState?> =
    repository.findById(id)
}