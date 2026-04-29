package com.example.photobasedtextrpg.core.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.photobasedtextrpg.core.data.db.GameState
import com.example.photobasedtextrpg.core.data.db.WorldSetting

data class AdventureHistory(
  val gameStateId: String,
  val worldSetting: WorldSetting,
  val clearedAt: Long
)