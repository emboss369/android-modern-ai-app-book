package com.example.photobasedtextrpg.core.ai

object PromptTemplates {

  // ── 世界設定生成プロンプト ─────────────────────────────────────────────
  fun worldSettingPrompt(imageDescription: String, genre: String): String = """
    あなたはRPGゲームマスターです。写真の特徴とジャンルをもとに、5つの項目を日本語で埋めてください。
    出力は以下の形式のみ。各行を「項目名:内容」の形式で出力し、説明文・空行・マークダウンは不要。

    写真: $imageDescription
    ジャンル: $genre

    舞台名:10文字以内の場所の名前をここに書く
    タイトル:15文字以内の物語タイトルをここに書く
    世界:100文字以内の世界説明をここに書く
    冒頭:あなたは〜で始まる80文字以内のナレーションをここに書く
    役割:20文字以内の主人公の役割をここに書く
    """.trimIndent()

  // ── プロンプトキャッシュ用プレフィックス ─────────────────────────────
  fun narrationCachePrefix(                     // ❶ ゲーム開始時に1回キャッシュ
    stageName: String, storyTitle: String,
    worldDescription: String, protagonistRole: String
  ): String = """
    あなたはRPGゲームマスターです。

    舞台名: $stageName
    タイトル: $storyTitle
    世界: $worldDescription
    役割: $protagonistRole
    """.trimIndent()

  // ── ターンナレーション＋選択肢 統合生成（キャッシュあり）───────────
  fun narrationAndChoicesWithCachePrompt(
    turnHistory: String, lastChoice: String
  ): String = """
    経緯を確認し、ナレーションと選択肢をJSON形式のみで日本語で返してください。
    ルール:
     - narration: 170文字以内。「その結果どうなったか」の具体的な場面描写。前の展開と異なる展開
     - choices: 3つ。各20文字以内
     - マークダウン・記号・余分な説明なし。JSONのみ

    経緯: $turnHistory
    選択: $lastChoice

    {"narration":"ナレーション本文","choices":["選択肢A","選択肢B","選択肢C"]}
    """.trimIndent()

  // ── ターンナレーション＋選択肢 統合生成（キャッシュなし・フォールバック）
  fun narrationAndChoicesPrompt(
    worldDescription: String, turnHistory: String, lastChoice: String
  ): String = """
    RPGのゲームマスターとして、ナレーションと選択肢をJSON形式のみで日本語で返してください。
    ルール:
     - narration: 170文字以内。「その結果どうなったか」の具体的な場面描写。前の展開と異なる展開
     - choices: 3つ。各20文字以内
     - マークダウン・記号・余分な説明なし。JSONのみ

    世界: $worldDescription
    経緯: $turnHistory
    選択: $lastChoice

    {"narration":"ナレーション本文","choices":["選択肢A","選択肢B","選択肢C"]}
    """.trimIndent()

  // ── ターンナレーション生成（キャッシュあり）──────────────────────────
  fun narrationWithCachePrompt(turnHistory: String, lastChoice: String): String = """
    経緯を確認し選択の結果としてさらなる物語の展開のナレーションを日本語170文字以内で書いてください。
    ルール:
     - マークダウン・記号・余分な説明なし。ナレーション本文のみ

    経緯: $turnHistory
    選択: $lastChoice

    ナレーション:
    """.trimIndent()

  // ── ターンナレーション生成（キャッシュなし）──────────────────────────
  fun narrationPrompt(
    worldDescription: String, turnHistory: String, lastChoice: String
  ): String = """
    RPGのゲームマスターとして経緯を確認し選択の結果としてさらなる物語の展開のナレーションを
    日本語170文字以内で書いてください。

    世界: $worldDescription
    経緯: $turnHistory
    選択: $lastChoice

    ナレーション:
    """.trimIndent()

  // ── 選択肢のみ生成（単独使用は非推奨）──────────────────────────────
  fun choicesPrompt(currentNarration: String): String = """
    RPGの選択肢を3つ、JSON形式のみで返してください。

    ナレーション:「$currentNarration」

    返答形式:
    {"choices":["選択肢A（20文字以内）","選択肢B（20文字以内）","選択肢C（20文字以内）"]}
    """.trimIndent()

  val DEFAULT_CHOICES = listOf("先に進む", "周りを調べる", "立ち止まって考える")
}
