package com.example.photobasedtextrpg.ui.worldgen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.photobasedtextrpg.R
import com.example.photobasedtextrpg.ui.components.Genre
import com.example.photobasedtextrpg.ui.components.GenreSelector
import com.example.photobasedtextrpg.ui.components.WoodButton
import com.example.photobasedtextrpg.ui.components.dropShadow
import com.example.photobasedtextrpg.ui.theme.PhotoBasedTextRPGTheme

sealed interface WorldGenUiState {
  data class Idle(val selectedGenre: Genre?) : WorldGenUiState
  data class Generating(
    val selectedGenre: Genre,
    val previewText: String,
    val imageDescription: String = ""
  ) : WorldGenUiState
  data class ReadyToNavigate(val worldSettingId: String) : WorldGenUiState
  data class Error(val selectedGenre: Genre?, val message: String) : WorldGenUiState
}

@Composable
fun WorldGenScreen(
  uiState: WorldGenUiState,
  onGenreSelected: (Genre) -> Unit,
  onRetry: () -> Unit,
  onNavigateToGame: (worldSettingId: String) -> Unit
) {
  // 現在のジャンルを特定（null = 未選択）
  val currentGenre: Genre? = when (uiState) {
    is WorldGenUiState.Idle -> uiState.selectedGenre
    is WorldGenUiState.Generating -> uiState.selectedGenre
    is WorldGenUiState.Error -> uiState.selectedGenre
    is WorldGenUiState.ReadyToNavigate -> null
  }

  // 自動遷移のサイドエフェクト
  LaunchedEffect(uiState) {
    if (uiState is WorldGenUiState.ReadyToNavigate) {
      onNavigateToGame(uiState.worldSettingId)
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    // ① ジャンル別背景（未選択時はファンタジー背景をデフォルト表示）
    val backgroundRes = when (currentGenre) {
      Genre.SF -> R.drawable.background_sf
      Genre.HORROR -> R.drawable.background_horror
      Genre.MYSTERY -> R.drawable.background_mistery
      else -> R.drawable.backgorund_fantasy // null（未選択）および FANTASY
    }
    Image(
      painter = painterResource(backgroundRes),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    // ② 上部バナー
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.TopCenter)
    ) {

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
      Column() {

        Text(
          text = "世界を創造中…",
          color = Color.White,
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily(Font(R.font.noto_sans_jp_bold, FontWeight.Bold)),
          modifier = Modifier
            .padding(24.dp)
        )
        // ジャンルセレクター（Idle / Error / Generating 時のみ表示、Generating 中はタップ不可）
        if (uiState !is WorldGenUiState.ReadyToNavigate) {
          Text(
            text = "ジャンル選択",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.noto_sans_jp_bold, FontWeight.Bold)),
            modifier = Modifier.padding(start =24.dp, bottom = 16.dp)


          )
          GenreSelector(
            selectedGenre = currentGenre,
            onGenreSelected = { genre ->
              // 生成中はタップ不可
              if (uiState !is WorldGenUiState.Generating) {
                onGenreSelected(genre)
              }
            }
          )
        }
      }




    }

    // ③ メインコンテンツ（中央）
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(top = 300.dp), // バナーと重ならないよう上部にパディングを確保
      horizontalAlignment = Alignment.CenterHorizontally
    ) {


      Spacer(modifier = Modifier.height(24.dp))

      // 生成中レイアウト
      if (uiState is WorldGenUiState.Generating) {
        // CircularProgressIndicator（グレーBoxの上）
        CircularProgressIndicator(
          modifier = Modifier.align(Alignment.CenterHorizontally),
          color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        // グレー角丸Box（画面幅の2/3）
        Box(
          modifier = Modifier
            .fillMaxWidth(2f / 3f)
            .background(
              color = Color(0xCC444444),
              shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
        ) {
          Column {
            // 固定文言
            Text(
              text = "写真を解析中...",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color.White
            )
            // 画像解析テキスト（届いたら表示）
            if (uiState.imageDescription.isNotEmpty()) {
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = uiState.imageDescription,
                fontSize = 13.sp,
                color = Color.White
              )
            }
          }
        }

        // プレビュー（幅を最初から固定。テキストは届いたら表示）
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(2f / 3f)) {
          Image(
            painter = painterResource(R.drawable.old_paper_medium),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
          )
          Text(
            text = uiState.previewText,
            modifier = Modifier.padding(24.dp),
            fontSize = 14.sp,
            color = Color.Black
          )
        }
      }

      // エラー表示（Error 時のみ表示）
      if (uiState is WorldGenUiState.Error) {
        Column(
          modifier = Modifier.padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(text = uiState.message, color = Color.White)
          Spacer(modifier = Modifier.height(8.dp))
          WoodButton(
            text = "もう一度試す",
            onClick = onRetry,
            modifier = Modifier
              .fillMaxWidth(0.6f)
              .height(52.dp)
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun WorldGenScreenIdlePreview() {
  PhotoBasedTextRPGTheme(
    content = {
      WorldGenScreen(
        uiState = WorldGenUiState.Idle(selectedGenre = null),
        onGenreSelected = {},
        onRetry = {},
        onNavigateToGame = {}
      )
    },
  )
}

@Preview(showBackground = true)
@Composable
fun WorldGenScreenGeneratingPreview() {
  PhotoBasedTextRPGTheme(
    content = {
      WorldGenScreen(
        uiState = WorldGenUiState.Generating(
          selectedGenre = Genre.SF,
          previewText = "宇宙の。。。",
          imageDescription = "無機質な金属廊下に青白い光が差し込む宇宙ステーション"
        ),
        onGenreSelected = {},
        onRetry = {},
        onNavigateToGame = {}
      )
    },
  )
}

@Preview(showBackground = true)
@Composable
fun WorldGenScreenErrorPreview() {
  PhotoBasedTextRPGTheme(
    content = {
      WorldGenScreen(
        uiState = WorldGenUiState.Error(
          selectedGenre = Genre.HORROR,
          message = "通信エラーが発生しました。"
        ),
        onGenreSelected = {},
        onRetry = {},
        onNavigateToGame = {}
      )
    },
  )
}
