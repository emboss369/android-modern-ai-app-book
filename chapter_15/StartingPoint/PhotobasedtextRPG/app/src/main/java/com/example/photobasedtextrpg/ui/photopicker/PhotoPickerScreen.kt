package com.example.photobasedtextrpg.ui.photopicker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.photobasedtextrpg.R
import com.example.photobasedtextrpg.ui.components.WoodButton
import com.example.photobasedtextrpg.ui.components.dropShadow
import com.example.photobasedtextrpg.ui.theme.PhotoBasedTextRPGTheme

sealed interface PhotoPickerUiState {
    data object Idle : PhotoPickerUiState
    data class PhotoSelected(val uri: Uri) : PhotoPickerUiState
}

@Composable
fun PhotoPickerScreen(
    uiState: PhotoPickerUiState,
    onPhotoSelected: (Uri) -> Unit,   // フォトピッカーで選択完了時
    onStartAdventure: (Uri) -> Unit   // CTA タップ時（WorldGenScreen へ）
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { onPhotoSelected(it) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // ① 上部バナー
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.banner_small),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .dropShadow(
                        color = Color.Black.copy(alpha = 0.6f),
                        borderRadius = 0.dp,
                        blurRadius = 16.dp,
                        offsetY = 6.dp
                    )
            )
            Text(
                text = "冒険の舞台を選ぶ",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.noto_sans_jp_bold, FontWeight.Bold)),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(24.dp)
            )
        }

        // ② 写真を選択するボタン（バナー直下）
        WoodButton(
            text = "写真を選択する",
            onClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            modifier = Modifier
                .padding(horizontal = 64.dp, vertical = 8.dp)
                .fillMaxWidth()
                .height(52.dp),
            fontSize = 26.sp
        )

        // ③ 写真選択エリア（中央・weight(1f) で残り高さを占有）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is PhotoPickerUiState.Idle -> {
                    // 未選択時 (Idle): フォトピッカーを起動するタップ可能エリア
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                    )
                }
                is PhotoPickerUiState.PhotoSelected -> {
                    // 選択済み時 (PhotoSelected): プレビュー表示
                    AsyncImage(
                        model = uiState.uri,
                        contentDescription = "選択した写真",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(2.5.dp, Color(0xFFD5B586), RoundedCornerShape(8.dp))
                            .clickable {
                                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                    )
                }
            }
        }
        Box() {

            // ④ 下部バナー
            Image(
                painter = painterResource(R.drawable.banner_small),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .dropShadow(
                        color = Color.Black.copy(alpha = 0.6f),
                        borderRadius = 0.dp,
                        blurRadius = 16.dp,
                        offsetY = (-6).dp
                    )
            )
            // ③ CTAボタン
            Image(
                painter = painterResource(R.drawable.start_adventure_photo),
                contentDescription = "この写真で冒険を始める",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
                    .alpha(if (uiState is PhotoPickerUiState.PhotoSelected) 1f else 0.4f)
                    .clickable(enabled = uiState is PhotoPickerUiState.PhotoSelected) {
                        if (uiState is PhotoPickerUiState.PhotoSelected) {
                            onStartAdventure(uiState.uri)
                        }
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PhotoPickerScreenIdlePreview() {
    PhotoBasedTextRPGTheme(
        content = {
            PhotoPickerScreen(
                uiState = PhotoPickerUiState.Idle,
                onPhotoSelected = {},
                onStartAdventure = {}
            )
        },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PhotoPickerScreenSelectedPreview() {
    PhotoBasedTextRPGTheme(
        content = {
            PhotoPickerScreen(
                uiState = PhotoPickerUiState.PhotoSelected(Uri.EMPTY),
                onPhotoSelected = {},
                onStartAdventure = {}
            )
        },
    )
}
