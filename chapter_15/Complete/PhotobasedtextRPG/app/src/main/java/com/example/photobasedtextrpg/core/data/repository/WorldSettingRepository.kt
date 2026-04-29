package com.example.photobasedtextrpg.core.data.repository

import com.example.photobasedtextrpg.core.data.db.WorldSetting
import com.example.photobasedtextrpg.core.data.db.WorldSettingDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorldSettingRepository(private val dao: WorldSettingDao) {

  suspend fun save(worldSetting: WorldSetting): Result<Unit> = runCatching {   // ❶
    dao.insert(worldSetting)
  }

  suspend fun findById(id: String): Result<WorldSetting?> = runCatching {
    dao.findById(id)
  }

  fun getAll(): Flow<Result<List<WorldSetting>>> =
    dao.getAll().map { Result.success(it) }
}