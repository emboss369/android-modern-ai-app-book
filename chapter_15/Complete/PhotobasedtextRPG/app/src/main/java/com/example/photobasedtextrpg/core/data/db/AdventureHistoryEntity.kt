package com.example.photobasedtextrpg.core.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class AdventureHistoryEntity(
  @Embedded val gameState: GameState,    // ❶ game_stateの全カラム
  @Relation(
    parentColumn = "worldSettingId",   // ❷ game_stateの参照元カラム
    entityColumn = "id"                // ❷ world_settingの参照先カラム
  )
  val worldSetting: WorldSetting
)