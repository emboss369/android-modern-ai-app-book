package com.example.photobasedtextrpg.ui.photopicker

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PhotoPickerViewModel : ViewModel() {

  private val _uiState = MutableStateFlow<PhotoPickerUiState>(PhotoPickerUiState.Idle)
  val uiState: StateFlow<PhotoPickerUiState> = _uiState.asStateFlow()

  fun onPhotoSelected(uri: Uri) {
    _uiState.value = PhotoPickerUiState.PhotoSelected(uri)
  }
}