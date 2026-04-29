package com.example.photobasedtextrpg.core.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TurnRecord(       // ❶ 各ターンの記録
  val turnNumber: Int,
  val narration: String,   // Nanoが生成したナレーション
  val choices: List<String>,  // Nanoが生成した選択肢3つ
  val userChoice: String   // プレイヤーが選んだ内容
)

@Entity(tableName = "game_state")
data class GameState(
  @PrimaryKey val id: String = UUID.randomUUID().toString(),
  val worldSettingId: String,         // WorldSettingへの外部キー
  val turns: List<TurnRecord>,        // ❷ ターン記録のリスト
  val currentTurn: Int,
  val createdAt: Long
)