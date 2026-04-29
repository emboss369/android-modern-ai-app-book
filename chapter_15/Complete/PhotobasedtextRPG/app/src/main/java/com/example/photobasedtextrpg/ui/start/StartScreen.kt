package com.example.photobasedtextrpg.ui.start

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.photobasedtextrpg.R
import com.example.photobasedtextrpg.ui.components.WoodButton
import com.example.photobasedtextrpg.ui.components.dropShadow
import com.example.photobasedtextrpg.ui.theme.PhotoBasedTextRPGTheme
import java.io.File

@Composable
fun StartScreen(
    onCameraResult: (Uri) -> Unit,
    onSelectPhoto: () -> Unit,
    onHistory: () -> Unit
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val photoUri = remember {
        if (isPreview) Uri.EMPTY else context.createTempImageUri()
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onCameraResult(photoUri)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(photoUri)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 最背面: 背景
        Image(
            painter = painterResource(id = R.drawable.start_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 最下部: 装飾背景
        Image(
            painter = painterResource(id = R.drawable.start_bottom),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .dropShadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    borderRadius = 0.dp,
                    blurRadius = 16.dp,
                    offsetY = (-6).dp
                ),
            contentScale = ContentScale.FillWidth
        )

        // 上部: タイトルロゴ
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .dropShadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    borderRadius = 0.dp,
                    blurRadius = 16.dp,
                    offsetY = 6.dp
                ),
        ) {
            Image(
                painter = painterResource(id = R.drawable.start_top),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )

            Image(
                painter = painterResource(id = R.drawable.top_title),
                contentDescription = "写真から始まるRPG: 霧の谷の遺構",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)
                    .fillMaxWidth(0.8f),
                contentScale = ContentScale.Fit
            )
        }

        // 中央: メインCTA (魔法陣 + 冒険を始める)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.magic_circle),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.
                size(500.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.start_adventure),
                contentDescription = "冒険を始める",
                modifier = Modifier
                    .size(300.dp)
                    .clickable {
                        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                            PackageManager.PERMISSION_GRANTED -> {
                                cameraLauncher.launch(photoUri)
                            }
                            else -> {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }
            )
        }

        // 下部ボタン群
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // サブボタン①「新しい写真を選ぶ」
            Image(
                painter = painterResource(id = R.drawable.start_select_photo),
                contentDescription = "新しい写真を選ぶ",
                modifier = Modifier
                    .width(200.dp)
                    .height(52.dp)
                    .clickable { onSelectPhoto() },
                contentScale = ContentScale.Fit
            )

            // サブボタン②「これまでの記録」
            WoodButton(
                text = "これまでの記録",
                onClick = onHistory,
                modifier = Modifier
                    .width(250.dp),
                fontSize = 18.sp
            )
        }
    }
}

private fun Context.createTempImageUri(): Uri {
    val file = File.createTempFile("photo_", ".jpg", cacheDir)
    return FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    PhotoBasedTextRPGTheme(
      content = {
        StartScreen(
          onCameraResult = {},
          onSelectPhoto = {},
          onHistory = {}
        )
      },
    )
}

