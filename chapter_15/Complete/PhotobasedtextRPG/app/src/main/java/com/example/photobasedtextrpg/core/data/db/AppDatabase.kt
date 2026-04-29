package com.example.photobasedtextrpg.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
  entities = [WorldSetting::class, GameState::class],
  version = 1,
  exportSchema = true    // ❶ スキーマをJSONファイルに書き出す
)
@TypeConverters(Converters::class)   // ❷ TypeConverterを登録
abstract class AppDatabase : RoomDatabase() {
  abstract fun worldSettingDao(): WorldSettingDao
  abstract fun gameStateDao(): GameStateDao
}