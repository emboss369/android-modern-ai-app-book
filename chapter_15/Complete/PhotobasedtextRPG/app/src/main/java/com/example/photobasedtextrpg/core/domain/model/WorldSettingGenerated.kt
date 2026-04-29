package com.example.photobasedtextrpg.core.domain.model

data class WorldSettingGenerated(     // ❶ パース結果のみ保持
  val stageName: String,
  val storyTitle: String,
  val worldDescription: String,
  val openingNarration: String,
  val protagonistRole: String
)

internal fun parseWorldSettingFromLines(raw: String): Result<WorldSettingGenerated> =
  runCatching {
    val map = raw.splitToSequence("\n")
      .mapNotNull { line ->
        // 半角':' と全角'：' の両方に対応する
        val idx = line.indexOfFirst { it == ':' || it == '：' }
        if (idx < 0) return@mapNotNull null
        val key = line.substring(0, idx).trim()
        val value = line.substring(idx + 1).trim()
        if (key.isBlank()) return@mapNotNull null
        key to value
      }
      .filter { (_, value) ->
        // テンプレートのヒント文字列（「ここに書く」など）を除外
        value.isNotBlank()
            && !value.contains("ここに書く")
            && !value.contains("文字以内")
            && !value.startsWith("（")
      }
      .toMap()
    WorldSettingGenerated(
      stageName        = map["舞台名"]  ?: error("舞台名が見つかりません"),
      storyTitle       = map["タイトル"] ?: error("タイトルが見つかりません"),
      worldDescription = map["世界"]    ?: error("世界が見つかりません"),
      openingNarration = map["冒頭"]    ?: error("冒頭が見つかりません"),
      protagonistRole  = map["役割"]    ?: error("役割が見つかりません"),
    )
  }