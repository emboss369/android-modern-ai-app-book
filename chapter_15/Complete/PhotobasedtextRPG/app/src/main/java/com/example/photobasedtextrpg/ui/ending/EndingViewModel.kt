package com.example.photobasedtextrpg.ui.ending

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.photobasedtextrpg.core.ai.nano.NanoWrapper
import com.example.photobasedtextrpg.core.data.db.AppDatabase
import com.example.photobasedtextrpg.core.data.repository.GameStateRepository
import com.example.photobasedtextrpg.core.data.repository.WorldSettingRepository
import com.example.photobasedtextrpg.core.domain.usecase.GetGameStateByIdUseCase
import com.example.photobasedtextrpg.core.domain.usecase.GetWorldSettingByIdUseCase
import com.example.photobasedtextrpg.ui.components.Genre
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ShareData(val imageUri: String, val text: String)   // ❶
class EndingViewModel(
  private val gameStateId: String,
  private val nanoWrapper: NanoWrapper,
  private val getGameStateByIdUseCase: GetGameStateByIdUseCase,
  private val getWorldSettingByIdUseCase: GetWorldSettingByIdUseCase
) : ViewModel() {

  private val _uiState =
    MutableStateFlow<EndingUiState>(EndingUiState.Loading)
  val uiState: StateFlow<EndingUiState> = _uiState.asStateFlow()

  private val _shareEvent = MutableSharedFlow<ShareData>()   // ❷
  val shareEvent: SharedFlow<ShareData> = _shareEvent.asSharedFlow()

  init {
    loadEnding()
  }

  private fun loadEnding() {
    viewModelScope.launch {
      val gameState = getGameStateByIdUseCase(gameStateId).getOrNull()
        ?: run {
          _uiState.value =
            EndingUiState.Error("冒険データの読み込みに失敗しました"); return@launch
        }

      val worldSetting =   // ❸
        getWorldSettingByIdUseCase(gameState.worldSettingId).getOrNull()
          ?: run {
            _uiState.value =
              EndingUiState.Error("世界設定の読み込みに失敗しました"); return@launch
          }

      val inputText = gameState.turns.joinToString("\n") { turn ->   // ❹
        "ターン${turn.turnNumber}: ${turn.narration} → ${turn.userChoice}"
      }
      val summary = nanoWrapper.summarize(inputText).getOrElse {
        gameState.turns.lastOrNull()?.narration   // ❺
          ?: "冒険は終わりました。"
      }

      val genre =
        runCatching { Genre.valueOf(worldSetting.genre) }.getOrElse { Genre.FANTASY }
      _uiState.value = EndingUiState.Success(
        storyTitle = worldSetting.storyTitle,
        imageUri = worldSetting.imageUri,
        genre = genre,
        summary = summary
      )
    }
  }

  fun onShare() {   // ❻
    val success = _uiState.value as? EndingUiState.Success ?: return
    viewModelScope.launch {
      _shareEvent.emit(
        ShareData(
          imageUri = success.imageUri,
          text = "「${success.storyTitle}」\n\n${success.summary}\n\n#写真から始まるRPG"
        )
      )
    }
  }

  override fun onCleared() {
    super.onCleared()
    nanoWrapper.close()
  }

  companion object {
    fun Factory(
      gameStateId: String,
      application: Application,
      db: AppDatabase
    ) =
      viewModelFactory {
        initializer {
          EndingViewModel(
            gameStateId = gameStateId,
            nanoWrapper = NanoWrapper.create(application),
            getGameStateByIdUseCase = GetGameStateByIdUseCase(
              GameStateRepository(db.gameStateDao())
            ),
            getWorldSettingByIdUseCase = GetWorldSettingByIdUseCase(
              WorldSettingRepository(db.worldSettingDao())
            )
          )
        }
      }
  }
}