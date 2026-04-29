package com.example.photobasedtextrpg.core.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
  @TypeConverter
  fun fromTurnRecordList(value: List<TurnRecord>): String =
    Json.encodeToString(value)   // ❶ List<TurnRecord> → JSON文字列

  @TypeConverter
  fun toTurnRecordList(value: String): List<TurnRecord> =
    Json.decodeFromString(value) // ❶ JSON文字列 → List<TurnRecord>
}