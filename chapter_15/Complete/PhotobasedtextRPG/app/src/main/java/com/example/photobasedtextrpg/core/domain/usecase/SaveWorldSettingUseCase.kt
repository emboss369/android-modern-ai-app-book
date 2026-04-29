package com.example.photobasedtextrpg.core.domain.usecase

import com.example.photobasedtextrpg.core.data.db.WorldSetting
import com.example.photobasedtextrpg.core.data.repository.WorldSettingRepository

class SaveWorldSettingUseCase(private val repository: WorldSettingRepository) {
  suspend operator fun invoke(worldSetting: WorldSetting): Result<Unit> =
    repository.save(worldSetting)
}