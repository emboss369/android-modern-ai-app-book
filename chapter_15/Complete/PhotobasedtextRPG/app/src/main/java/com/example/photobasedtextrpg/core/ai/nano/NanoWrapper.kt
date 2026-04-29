package com.example.photobasedtextrpg.core.ai.nano

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.util.Log.e
import com.example.photobasedtextrpg.core.ai.NarrationAndChoices
import com.example.photobasedtextrpg.core.ai.PromptTemplates
import com.example.photobasedtextrpg.core.ai.parseChoicesFromJson
import com.example.photobasedtextrpg.core.ai.parseNarrationAndChoicesFromJson
import com.example.photobasedtextrpg.core.domain.model.WorldSettingGenerated
import com.example.photobasedtextrpg.core.domain.model.parseWorldSettingFromLines
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.imagedescription.ImageDescriber
import com.google.mlkit.genai.imagedescription.ImageDescriberOptions
import com.google.mlkit.genai.imagedescription.ImageDescription
import com.google.mlkit.genai.imagedescription.ImageDescriptionRequest
import com.google.mlkit.genai.prompt.Generation
import com.google.mlkit.genai.prompt.GenerativeModel
import com.google.mlkit.genai.prompt.PromptPrefix
import com.google.mlkit.genai.prompt.TextPart
import com.google.mlkit.genai.prompt.createCachedContextRequest
import com.google.mlkit.genai.prompt.generateContentRequest
import com.google.mlkit.genai.summarization.Summarization
import com.google.mlkit.genai.summarization.SummarizationRequest
import com.google.mlkit.genai.summarization.Summarizer
import com.google.mlkit.genai.summarization.SummarizerOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NanoWrapper(
  private val imageDescriber: ImageDescriber,  // ❶
  private val nanoModel: GenerativeModel,
  private val summarizer: Summarizer
) {
  companion object {
    private const val TAG = "NanoWrapper"
    fun create(context: Context): NanoWrapper {  // ❷
      val imageDescriber = ImageDescription.getClient(
        ImageDescriberOptions.builder(context).build()
      )
      val nanoModel = Generation.getClient()   // ❸
      val summarizer = Summarization.getClient(
        SummarizerOptions.builder(context)
          .setInputType(SummarizerOptions.InputType.ARTICLE)
          .setOutputType(SummarizerOptions.OutputType.THREE_BULLETS)
          .setLanguage(SummarizerOptions.Language.JAPANESE)
          .build()
      )
      return NanoWrapper(imageDescriber, nanoModel, summarizer)
    }
  }

  suspend fun isAvailable(): Boolean = runCatching {
    val imageStatus =
      imageDescriber.checkFeatureStatus().await()   // ❶
    if (imageStatus == FeatureStatus.UNAVAILABLE) return false

    val promptStatus =
      nanoModel.checkStatus()                      // ❷
    promptStatus != FeatureStatus.UNAVAILABLE
  }.getOrElse { false }

  suspend fun describeImage(bitmap: Bitmap): Result<String> =
    runCatching {
      val request = ImageDescriptionRequest.builder(bitmap).build()
      val result = imageDescriber.runInference(request).await()
      result.description.ifBlank {
        throw RuntimeException("ImageDescriber が空の説明を返しました")
      }
    }.onFailure { e ->
      Log.e(TAG, "describeImage: failed", e)
    }

  private suspend fun <T> retryOnBusy(
    label: String,
    maxRetries: Int = 5,
    delayMs: Long = 3_000L,
    block: suspend () -> T
  ): T {
    var lastException: Throwable =
      RuntimeException("retryOnBusy: unreachable")
    repeat(maxRetries) { attempt ->
      try {
        return block()
      } catch (e: Exception) {
        val isBusy = e.message?.contains("BUSY") == true ||
            e.message?.contains("ErrorCode 9") == true  // ❶
        if (isBusy && attempt < maxRetries - 1) {
          Log.w(
            TAG,
            "$label: BUSY (attempt ${attempt + 1}/$maxRetries), wait ${delayMs}ms"
          )
          delay(delayMs)       // ❷
          lastException = e
        } else throw e
      }
    }
    throw lastException
  }

  suspend fun generateWorldSetting(
    imageDescription: String,
    genre: String
  ): Result<WorldSettingGenerated> {
    val prompt =
      PromptTemplates.worldSettingPrompt(imageDescription, genre)
    var lastError: Throwable =
      RuntimeException("WorldSetting生成に失敗しました")

    repeat(3) { attempt ->
      val rawResult = runCatching {
        val response =
          retryOnBusy("generateWorldSetting attempt $attempt") {
            nanoModel.generateContent(
              generateContentRequest(TextPart(prompt)) {
                temperature = 0.5f
              }
            )
          }
        response.candidates.firstOrNull()?.text
          ?: throw RuntimeException("Nano が空の応答を返しました")
      }
      val raw =
        rawResult.getOrElse { e -> lastError = e; return@repeat }
      val parsed = parseWorldSettingFromLines(raw)
      if (parsed.isSuccess) return parsed    // ❶ パース成功で即return
      lastError = parsed.exceptionOrNull() ?: lastError
    }
    return Result.failure(lastError)
  }

  suspend fun generateWorldSettingStreaming(
    imageDescription: String,
    genre: String,
    onPartialPreview: (String) -> Unit     // ❶
  ): Result<WorldSettingGenerated> {
    val prompt =
      PromptTemplates.worldSettingPrompt(imageDescription, genre)
    var lastRawText = ""

    val streamResult = runCatching {
      nanoModel.generateContentStream(prompt)
        .collect { response ->  // ❷
          val text =
            response.candidates.firstOrNull()?.text ?: return@collect
          lastRawText = text   // ❸
          val preview = extractNarrationPreview(text)
          if (preview.isNotEmpty()) onPartialPreview(preview)
        }
    }
    if (streamResult.isFailure) {
      return generateWorldSetting(imageDescription, genre)  // ❹
    }

    val parsed = parseWorldSettingFromLines(lastRawText)
    return if (parsed.isSuccess) parsed
    else generateWorldSetting(imageDescription, genre)
  }

  private fun extractNarrationPreview(raw: String): String { // ❺
    for (marker in listOf("冒頭:", "冒頭：")) {
      val idx = raw.indexOf(marker)
      if (idx >= 0) return raw.substring(idx + marker.length)
        .substringBefore("\n").trim()
    }
    return ""
  }

  suspend fun createNarrationCache(
    cacheName: String,        // worldSetting.id をキャッシュ名に使う
    stageName: String,
    storyTitle: String,
    worldDescription: String,
    protagonistRole: String
  ): Result<String> = runCatching {
    val prefix = PromptTemplates.narrationCachePrefix(
      stageName, storyTitle, worldDescription, protagonistRole
    )
    nanoModel.caches.create(                  // ❶
      createCachedContextRequest(cacheName, PromptPrefix(prefix))
    )
    cacheName
  }

  suspend fun deleteNarrationCache(cacheName: String) {
    runCatching { nanoModel.caches.delete(cacheName) }  // ❷
  }

  suspend fun generateNarrationAndChoicesWithCache(
    cacheName: String,
    turnHistory: String,
    lastChoice: String,
    fallbackWorldDescription: String
  ): Result<NarrationAndChoices> = runCatching {
    val suffix = PromptTemplates.narrationAndChoicesWithCachePrompt(
      turnHistory, lastChoice
    )
    val request = generateContentRequest(TextPart(suffix)) {
      cachedContextName = cacheName   // ❶
      temperature = 0.8f
    }
    val raw = retryOnBusy("generateNarrationAndChoicesWithCache") {
      nanoModel.generateContent(request)
    }.candidates.firstOrNull()?.text ?: throw RuntimeException(
      "Nano が空の応答を返しました"
    )
    parseNarrationAndChoicesFromJson(raw).getOrThrow()
  }.recoverCatching { e ->
    generateNarrationAndChoices(  // ❷
      fallbackWorldDescription, turnHistory, lastChoice
    ).getOrThrow()
  }

  suspend fun generateNarrationAndChoices(  // ❸
    worldDescription: String,
    turnHistory: String,
    lastChoice: String
  ): Result<NarrationAndChoices> = runCatching {
    val prompt = PromptTemplates.narrationAndChoicesPrompt(
      worldDescription, turnHistory, lastChoice
    )
    val raw = retryOnBusy("generateNarrationAndChoices") {
      nanoModel.generateContent(
        generateContentRequest(
          TextPart(prompt)
        ) {
          temperature = 0.8f
        })
    }.candidates.firstOrNull()?.text
      ?: throw RuntimeException("Nano が空の応答を返しました")
    Log.d(TAG, "generateNarrationAndChoices: raw=\n---\n$raw\n---")
    parseNarrationAndChoicesFromJson(raw).getOrThrow()
  }.onFailure { e ->
    Log.e(TAG, "generateNarrationAndChoices: failed", e)
  }

  suspend fun summarize(text: String): Result<String> = runCatching {
    val truncated = text.take(3_000)    // ❶
    val request = SummarizationRequest.builder(truncated).build()
    val result = summarizer.runInference(request).await()
    result.summary.ifBlank { throw RuntimeException("Summarizer が空の要約を返しました") }
  }.onFailure { e ->
    Log.e(TAG, "summarize: failed", e)
  }

  suspend fun generateChoices(narration: String): Result<List<String>> = runCatching {
    val prompt = PromptTemplates.choicesPrompt(narration)
    val rawJson = withTimeout(60_000L) {   // ❶
      val response = retryOnBusy("generateChoices") {
        nanoModel.generateContent(generateContentRequest(TextPart(prompt)) {
          temperature = 0.9f   // ❷
        })
      }
      response.candidates.firstOrNull()?.text
        ?: throw RuntimeException("Nano が空の応答を返しました")
    }
    parseChoicesFromJson(rawJson).getOrThrow()
  }.onFailure { e ->
    Log.e(TAG, "generateChoices: failed", e)
  }

  fun close() {
    imageDescriber.close()
    nanoModel.close()
    summarizer.close()
  }
}



private suspend fun <T> ListenableFuture<T>.await(): T =
  suspendCancellableCoroutine { cont ->      // ❶
    addListener(
      {
        try {
          cont.resume(get())
        }     // ❷
        catch (e: Exception) {
          if (cont.isActive) cont.resumeWithException(e)  // ❸
        }
      },
      { command -> command.run() }
    )
    cont.invokeOnCancellation { cancel(true) }  // ❹
  }

