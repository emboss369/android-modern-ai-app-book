package com.example.mycamera

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermissionHandler(onGranted: (Boolean) -> Unit) {
  val context = LocalContext.current
  val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { granted ->
    onGranted(granted)
  }
  LaunchedEffect(Unit) {
    if (ContextCompat.checkSelfPermission(
        context, android.Manifest.permission.CAMERA
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      onGranted(true)
    } else {
      launcher.launch(android.Manifest.permission.CAMERA)
    }
  }
}