package com.example.mycamera

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun rememberCameraController(): LifecycleCameraController {
  val context = LocalContext.current
  return remember {
    LifecycleCameraController(context).apply {
      setEnabledUseCases(LifecycleCameraController.IMAGE_CAPTURE)
    }
  }
}

fun LifecycleCameraController.takePhoto(
  context: Context
) {
  val name = SimpleDateFormat(
    "yyyy-MM-dd-HH-mm-ss-SSS",
    Locale.US
  ).format(System.currentTimeMillis())

  val contentValues = ContentValues().apply {
    put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, name)
    put(android.provider.MediaStore.MediaColumns.MIME_TYPE,
      "image/jpeg")
    put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH,
      "Pictures/CameraX-Image")
  }

  val outputOptions = ImageCapture.OutputFileOptions.Builder(
    context.contentResolver,
    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    contentValues
  ).build()

  takePicture(
    outputOptions,
    ContextCompat.getMainExecutor(context),
    object : ImageCapture.OnImageSavedCallback {
      override fun onError(exc: ImageCaptureException) {
        val msg = "Photo capture failed: ${exc.message}"
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        Log.e("Camera", msg, exc)
      }

      override fun onImageSaved(output: ImageCapture.OutputFileResults) {
        val msg = "Photo capture succeeded: ${output.savedUri}"
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        Log.d("Camera", msg)
        Intent(Intent.ACTION_SEND).also { share ->
          share.type = "image/*"
          share.putExtra(Intent.EXTRA_STREAM, output.savedUri)
          context.startActivity(
            Intent.createChooser(
              share,
              "Share to"
            )
          )
        }
      }
    }
  )
}