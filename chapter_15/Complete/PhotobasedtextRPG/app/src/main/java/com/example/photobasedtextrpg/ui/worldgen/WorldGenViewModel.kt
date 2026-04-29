package com.example.photobasedtextrpg.ui.worldgen

import android.app.Application
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.photobasedtextrpg.core.ai.nano.NanoWrapper
import com.example.photobasedtextrpg.core.data.db.AppDatabase
import com.example.photobasedtextrpg.core.data.db.WorldSetting
import com.example.photobasedtextrpg.core.data.repository.WorldSettingRepository
import com.example.photobasedtextrpg.core.domain.usecase.SaveWorldSettingUseCase
import com.example.photobasedtextrpg.ui.components.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorldGenViewModel(
  private val imageUri: String,
  private val application: Application,
  private val nanoWrapper: NanoWrapper,
  private val saveWorldSettingUseCase: SaveWorldSettingUseCase
) : ViewModel() {

  private val _uiState =
    MutableStateFlow<WorldGenUiState>(WorldGenUiState.Idle(null))
  val uiState: StateFlow<WorldGenUiState> = _uiState.asStateFlow()

  fun onGenreSelected(genre: Genre) {
    if (_uiState.value is WorldGenUiState.Idle ||
      _uiState.value is WorldGenUiState.Error
    ) {
      startGeneration(genre)
    }
  }

  fun onRetry() {
    (_uiState.value as? WorldGenUiState.Error)?.selectedGenre?.let {
      startGeneration(
        it
      )
    }
  }

  private fun startGeneration(genre: Genre) {
    viewModelScope.launch {
      // Step 1: Nano が利用可能かを確認する
      if (!nanoWrapper.isAvailable()) {
        _uiState.value = WorldGenUiState.Error(
          genre,
          "Gemini Nanoモデルの準備ができていません。" +
              "非対応端末またはモデルのダウンロード中の可能性があります。"
        )
        return@launch
      }

      _uiState.value = WorldGenUiState.Generating(genre, "")

      // Nano で写真を解析（失敗しても空文字で続行）❶
      val imageDescription = runCatching {
        val bitmap = loadBitmapFromUri() ?: return@runCatching ""
        nanoWrapper.describeImage(bitmap).getOrElse { "" }
      }.getOrElse { "" }

      _uiState.value =
        WorldGenUiState.Generating(genre, "", imageDescription)

      // ストリーミングで世界設定を生成。冒頭ナレーションの文字が届くたびにUIを更新
      val generatedResult = nanoWrapper.generateWorldSettingStreaming(
        imageDescription, genre.name
      ) { partialNarration ->   // ❷
        _uiState.value = WorldGenUiState.Generating(
          genre,
          partialNarration,
          imageDescription
        )
      }

      if (generatedResult.isFailure) {
        _uiState.value = WorldGenUiState.Error(
          genre,
          "世界設定の生成に失敗しました。"
        )
        return@launch
      }
      val generated = generatedResult.getOrThrow()

      // Step 4: Room DB に保存
      val worldSetting = WorldSetting(
        imageUri = imageUri,
        genre = genre.name,
        stageName = generated.stageName,
        storyTitle = generated.storyTitle,
        worldDescription = generated.worldDescription,
        openingNarration = generated.openingNarration,
        protagonistRole = generated.protagonistRole
      )
      if (saveWorldSettingUseCase(worldSetting).isFailure) {
        _uiState.value =
          WorldGenUiState.Error(genre, "データの保存に失敗しました。")
        return@launch
      }

      // 1.5秒プレビューを表示してから GameScreen へ遷移
      _uiState.value =
        WorldGenUiState.Generating(genre, generated.openingNarration)
      delay(1_500L)   // ❸
      _uiState.value =
        WorldGenUiState.ReadyToNavigate(worldSetting.id)  // ❹
    }
  }

  private suspend fun loadBitmapFromUri(): Bitmap? = withContext(
    Dispatchers.IO   // ❺
  ) {
    runCatching {
      ImageDecoder.decodeBitmap(
        ImageDecoder.createSource(
          application.contentResolver,
          Uri.parse(imageUri)
        )
      )
    }.getOrNull()
  }

  override fun onCleared() {
    super.onCleared()
    nanoWrapper.close()   // ❻
  }

  companion object {
    fun Factory(
      imageUri: String,
      application: Application,
      db: AppDatabase
    ) =
      viewModelFactory {
        initializer {
          WorldGenViewModel(
            imageUri = imageUri,
            application = application,
            nanoWrapper = NanoWrapper.create(application),
            saveWorldSettingUseCase = SaveWorldSettingUseCase(
              WorldSettingRepository(db.worldSettingDao())
            )
          )
        }
      }
  }
}