package com.example.photobasedtextrpg.core.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "world_setting")
data class WorldSetting(
  @PrimaryKey val id: String = UUID.randomUUID().toString(),
  val imageUri: String,          // 冒険に使った写真のUri
  val genre: String,             // ジャンル（Genre enum の name）
  val stageName: String,         // 舞台名
  val storyTitle: String,        // 物語タイトル
  val worldDescription: String,  // 世界説明
  val openingNarration: String,  // 冒頭ナレーション
  val protagonistRole: String,   // 主人公の役割
  val createdAt: Long = System.currentTimeMillis()
)