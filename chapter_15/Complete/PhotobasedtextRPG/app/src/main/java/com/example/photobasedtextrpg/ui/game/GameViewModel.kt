package com.example.photobasedtextrpg.ui.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.photobasedtextrpg.core.ai.PromptTemplates
import com.example.photobasedtextrpg.core.ai.nano.NanoWrapper
import com.example.photobasedtextrpg.core.data.db.AppDatabase
import com.example.photobasedtextrpg.core.data.db.GameState
import com.example.photobasedtextrpg.core.data.db.TurnRecord
import com.example.photobasedtextrpg.core.data.db.WorldSetting
import com.example.photobasedtextrpg.core.data.repository.GameStateRepository
import com.example.photobasedtextrpg.core.data.repository.WorldSettingRepository
import com.example.photobasedtextrpg.core.domain.usecase.GetWorldSettingByIdUseCase
import com.example.photobasedtextrpg.core.domain.usecase.SaveGameStateUseCase
import com.example.photobasedtextrpg.core.domain.usecase.UpdateGameStateUseCase
import com.example.photobasedtextrpg.ui.components.Genre
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
  private val worldSettingId: String,
  private val nanoWrapper: NanoWrapper,
  private val getWorldSettingByIdUseCase: GetWorldSettingByIdUseCase,
  private val saveGameStateUseCase: SaveGameStateUseCase,
  private val updateGameStateUseCase: UpdateGameStateUseCase
) : ViewModel() {

  private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
  val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

  private val _navigateToEnding = MutableSharedFlow<String>()    // ❶
  val navigateToEnding: SharedFlow<String> = _navigateToEnding.asSharedFlow()

  private var worldSetting: WorldSetting? = null
  private var gameState: GameState? = null
  private var narrationCacheName: String? = null   // ❷

  init { startGame() }   // ❸

  private fun startGame() {
    viewModelScope.launch {
      val setting = getWorldSettingByIdUseCase(worldSettingId).getOrNull()
        ?.also { worldSetting = it }
        ?: run { _uiState.value = GameUiState.Error("世界設定の読み込みに失敗しました"); return@launch }

      // 世界設定をプレフィックスとしてキャッシュ（全ターンで使いまわす）
      narrationCacheName = nanoWrapper.createNarrationCache(
        cacheName = worldSettingId,
        stageName = setting.stageName,
        storyTitle = setting.storyTitle,
        worldDescription = setting.worldDescription,
        protagonistRole = setting.protagonistRole
      ).getOrNull()

      // 初期GameStateを保存（ターン0の空状態）
      val initialState = GameState(
        worldSettingId = worldSettingId,
        turns = emptyList(),
        currentTurn = 0,
        createdAt = System.currentTimeMillis()
      )
      saveGameStateUseCase(initialState)
      gameState = initialState

      val genre = runCatching { Genre.valueOf(setting.genre) }.getOrElse { Genre.FANTASY }

      // 冒頭ナレーションを先に表示し、選択肢を裏で生成する
      _uiState.value = GameUiState.Playing(
        stageName = setting.stageName, genre = genre, currentTurn = 1,
        narration = setting.openingNarration, choices = emptyList(),
        isChoicesLoading = true   // ❹
      )
      val choices = nanoWrapper.generateChoices(setting.openingNarration)
        .getOrElse { PromptTemplates.DEFAULT_CHOICES }   // ❺
      _uiState.value = GameUiState.Playing(
        stageName = setting.stageName, genre = genre, currentTurn = 1,
        narration = setting.openingNarration, choices = choices, isChoicesLoading = false
      )
    }
  }

  fun onChoiceSelected(choice: String) = handleUserAction(choice)   // ❻
  fun onFreeInput(text: String) = handleUserAction(text)

  private fun handleUserAction(userChoice: String) {
    val current = _uiState.value as? GameUiState.Playing ?: return
    val setting = worldSetting ?: return
    val state = gameState ?: return

    viewModelScope.launch {
      // 現在のターンを記録する
      val turnRecord = TurnRecord(
        turnNumber = current.currentTurn,
        narration = current.narration,
        choices = current.choices,
        userChoice = userChoice
      )
      val updatedTurns = state.turns + turnRecord
      val updatedState = state.copy(turns = updatedTurns, currentTurn = updatedTurns.size)
      gameState = updatedState
      updateGameStateUseCase(updatedState)

      if (updatedTurns.size >= 5) {
        // 5ターン完了 → キャッシュを削除してエンディングへ
        narrationCacheName?.let { nanoWrapper.deleteNarrationCache(it) }
        narrationCacheName = null
        _navigateToEnding.emit(updatedState.id)
        return@launch
      }

      val nextTurn = updatedTurns.size + 1
      _uiState.value = GameUiState.Playing(
        stageName = setting.stageName, genre = current.genre, currentTurn = nextTurn,
        narration = current.narration, choices = emptyList(), isChoicesLoading = true
      )

      val turnHistory = updatedTurns.joinToString("\n") { turn ->   // ❼
        "ターン${turn.turnNumber}: ${turn.narration} → ${turn.userChoice}"
      }

      // ナレーション＋選択肢を1回の推論でまとめて生成
      val cacheName = narrationCacheName
      val result = if (cacheName != null) {   // ❽
        nanoWrapper.generateNarrationAndChoicesWithCache(
          cacheName = cacheName, turnHistory = turnHistory,
          lastChoice = userChoice, fallbackWorldDescription = setting.worldDescription
        )
      } else {
        nanoWrapper.generateNarrationAndChoices(
          worldDescription = setting.worldDescription,
          turnHistory = turnHistory, lastChoice = userChoice
        )
      }.getOrElse {
        _uiState.value = GameUiState.Error("ナレーションの生成に失敗しました。アプリを再起動してください。")
        return@launch
      }

      _uiState.value = GameUiState.Playing(
        stageName = setting.stageName, genre = current.genre, currentTurn = nextTurn,
        narration = result.narration, choices = result.choices, isChoicesLoading = false
      )
    }
  }

  override fun onCleared() {
    super.onCleared()
    nanoWrapper.close()
  }

  companion object {
    fun Factory(worldSettingId: String, application: Application, db: AppDatabase) =
      viewModelFactory {
        initializer {
          GameViewModel(
            worldSettingId = worldSettingId,
            nanoWrapper = NanoWrapper.create(application),
            getWorldSettingByIdUseCase = GetWorldSettingByIdUseCase(
              WorldSettingRepository(db.worldSettingDao())
            ),
            saveGameStateUseCase = SaveGameStateUseCase(
              GameStateRepository(db.gameStateDao())
            ),
            updateGameStateUseCase = UpdateGameStateUseCase(
              GameStateRepository(db.gameStateDao())
            )
          )
        }
      }
  }
}