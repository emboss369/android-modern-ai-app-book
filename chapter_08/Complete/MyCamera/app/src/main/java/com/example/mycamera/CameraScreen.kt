package com.example.mycamera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun CameraScreen(modifier: Modifier = Modifier) {
  var isGranted by remember { mutableStateOf(false) }
  val context = LocalContext.current
  val controller = rememberCameraController()
  PermissionHandler { granted ->
    isGranted = granted
  }

  Box(modifier = modifier.fillMaxSize()) {
    if (isGranted) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
      ) {
        PreviewCamera(controller)
        TakePhoto(modifier = Modifier.padding(16.dp)) {
          controller.takePhoto(context)
        }
      }
    } else {
      Text(
        text = "カメラの権限がありません",
        modifier = Modifier.align(Alignment.Center)
      )
    }
  }
}