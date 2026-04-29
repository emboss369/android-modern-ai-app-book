package com.example.photobasedtextrpg.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.photobasedtextrpg.core.data.db.AppDatabase
import com.example.photobasedtextrpg.core.data.repository.GameStateRepository
import com.example.photobasedtextrpg.core.domain.model.AdventureHistory
import com.example.photobasedtextrpg.core.domain.usecase.GetCompletedAdventuresUseCase
import com.example.photobasedtextrpg.ui.components.Genre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.collections.filter

class HistoryViewModel(
  private val getCompletedAdventuresUseCase: GetCompletedAdventuresUseCase
) : ViewModel() {

  private val _uiState =
    MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
  val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

  private var selectedGenre: Genre = Genre.FANTASY  // ❶
  private var allHistory: List<AdventureHistory> = emptyList()

  init {
    observeHistory()
  }

  private fun observeHistory() {
    getCompletedAdventuresUseCase()
      .onEach { result ->
        allHistory = result.getOrElse { emptyList() }
        updateUiState()
      }
      .launchIn(viewModelScope)        // ❷
  }

  fun onGenreSelected(genre: Genre) {
    selectedGenre = genre
    updateUiState()                    // ❸
  }

  private fun updateUiState() {
    val filtered =
      allHistory.filter { it.worldSetting.genre == selectedGenre.name }
    _uiState.value = if (filtered.isEmpty()) {
      HistoryUiState.Empty(selectedGenre)  // ❹
    } else {
      HistoryUiState.Success(
        historyList = filtered.map { history ->
          AdventureHistoryItem(
            gameStateId = history.gameStateId,
            storyTitle = history.worldSetting.storyTitle,
            imageUri = history.worldSetting.imageUri,
            genre = history.worldSetting.genre,
            clearedAt = history.clearedAt
          )
        },
        selectedGenre = selectedGenre
      )
    }
  }

  companion object {
    fun Factory(db: AppDatabase) = viewModelFactory {
      initializer {
        HistoryViewModel(
          getCompletedAdventuresUseCase = GetCompletedAdventuresUseCase(
            GameStateRepository(db.gameStateDao())
          )
        )
      }
    }
  }
}