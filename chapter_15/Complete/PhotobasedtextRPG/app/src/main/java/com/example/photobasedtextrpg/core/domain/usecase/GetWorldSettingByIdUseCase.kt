package com.example.photobasedtextrpg.core.domain.usecase

import com.example.photobasedtextrpg.core.data.db.WorldSetting
import com.example.photobasedtextrpg.core.data.repository.WorldSettingRepository

class GetWorldSettingByIdUseCase(private val repository: WorldSettingRepository) {
  suspend operator fun invoke(id: String): Result<WorldSetting?> =
    repository.findById(id)
}