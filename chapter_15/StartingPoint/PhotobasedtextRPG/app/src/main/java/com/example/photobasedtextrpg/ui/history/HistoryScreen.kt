package com.example.photobasedtextrpg.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.photobasedtextrpg.R
import com.example.photobasedtextrpg.ui.components.Genre
import com.example.photobasedtextrpg.ui.components.GenreSelector
import com.example.photobasedtextrpg.ui.components.WoodButton
import com.example.photobasedtextrpg.ui.components.dropShadow
import com.example.photobasedtextrpg.ui.theme.PhotoBasedTextRPGTheme

sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data class Success(
        val historyList: List<AdventureHistoryItem>,
        val selectedGenre: Genre
    ) : HistoryUiState
    data class Empty(val selectedGenre: Genre) : HistoryUiState
}

data class AdventureHistoryItem(
    val gameStateId: String,
    val storyTitle: String,
    val imageUri: String,
    val genre: String,          // "FANTASY" / "SF" / "HORROR" / "MYSTERY"
    val clearedAt: Long         // エポックミリ秒
)

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onGenreSelected: (Genre) -> Unit,
    onHistoryItemClick: (gameStateId: String) -> Unit,
    onBack: () -> Unit
) {
    val currentGenre = when (uiState) {
        is HistoryUiState.Success -> uiState.selectedGenre
        is HistoryUiState.Empty -> uiState.selectedGenre
        else -> Genre.FANTASY
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        // 上部バナー + ジャンルセレクター
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.banner_big),
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "これまでの記録",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.noto_sans_jp_bold, FontWeight.Bold)),
                    modifier = Modifier.padding(start = 24.dp, top = 8.dp, bottom = 16.dp)
                )
                Text(
                    text = "ジャンル別フィルター",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.noto_sans_jp_bold, FontWeight.Bold)),
                    modifier = Modifier.padding(start = 24.dp, bottom = 4.dp)
                )
                GenreSelector(
                    selectedGenre = currentGenre,
                    onGenreSelected = onGenreSelected,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // 記録リスト
        Box(modifier = Modifier.weight(1f)) {
            when (uiState) {
                is HistoryUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is HistoryUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "まだ冒険の記録がありません",
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                is HistoryUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.historyList) { item ->
                            HistoryCard(
                                item = item,
                                onClick = { onHistoryItemClick(item.gameStateId) }
                            )
                        }
                    }
                }
            }
        }

        // 下部バナー + 戻るボタン
        Box(modifier = Modifier.fillMaxWidth()) {
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
            WoodButton(
                text = "スタート画面へ戻る",
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .align(Alignment.Center)
                    .padding(vertical = 8.dp),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
private fun HistoryCard(item: AdventureHistoryItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // 背景: old_paper_middle
        Image(
            painter = painterResource(R.drawable.old_paper_middle),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 写真サムネイル
            AsyncImage(
                model = item.imageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            // テキスト情報
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.storyTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3D2B1F),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDate(item.clearedAt),
                    fontSize = 12.sp,
                    color = Color(0xFF7A5C3A)
                )
            }
            // ジャンルアイコン
            val genre = Genre.entries.find { it.name == item.genre }
            if (genre != null) {
                Image(
                    painter = painterResource(genre.iconRes),
                    contentDescription = genre.labelJa,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

private fun formatDate(epochMillis: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy/MM/dd HH:mm", java.util.Locale.JAPAN)
    return sdf.format(java.util.Date(epochMillis))
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenSuccessPreview() {
    PhotoBasedTextRPGTheme(
      content = {
        HistoryScreen(
          uiState = HistoryUiState.Success(
            historyList = listOf(
              AdventureHistoryItem(
                gameStateId = "1",
                storyTitle = "霧の谷の遺構",
                imageUri = "",
                genre = "FANTASY",
                clearedAt = System.currentTimeMillis()
              )
            ),
            selectedGenre = Genre.FANTASY
          ),
          onGenreSelected = {},
          onHistoryItemClick = {},
          onBack = {}
        )
      },
    )
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenEmptyPreview() {
    PhotoBasedTextRPGTheme(
      content = {
        HistoryScreen(
          uiState = HistoryUiState.Empty(Genre.HORROR),
          onGenreSelected = {},
          onHistoryItemClick = {},
          onBack = {}
        )
      },
    )
}
