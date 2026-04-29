package com.example.photobasedtextrpg.core.ai

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@kotlinx.serialization.Serializable
data class ChoicesResponse(val choices: List<String>)

@Serializable
data class NarrationAndChoices(val narration: String, val choices: List<String>)

internal fun parseNarrationAndChoicesFromJson(raw: String): Result<NarrationAndChoices> =
  runCatching {
    val stripped = raw.trim()
      .removePrefix("```json").removePrefix("```")  // ❶ Markdownの除去
      .removeSuffix("```").trim()
    // ❷ JSONオブジェクトの範囲を抽出（前後に余分なテキストがある場合に対応）
    val start = stripped.indexOf('{')
    val end = stripped.lastIndexOf('}')
    require(start >= 0 && end > start) { "JSONオブジェクトが見つかりません" }
    val jsonStr = stripped.substring(start, end + 1)
    val lenient = Json { ignoreUnknownKeys = true }
    val response = lenient.decodeFromString<NarrationAndChoices>(jsonStr)
    require(response.narration.isNotBlank()) { "ナレーションが空です" }
    require(response.choices.size == 3) { "選択肢が3つではありません" }
    response
  }

internal fun parseChoicesFromJson(raw: String): Result<List<String>> = runCatching {
  val json = raw.trim().removePrefix("```json").removePrefix("```")
    .removeSuffix("```").trim()
  val response = Json.decodeFromString<ChoicesResponse>(json)
  require(response.choices.size == 3) { "選択肢は3つ必要です" }
  response.choices
}