package com.example.photobasedtextrpg.ui.ending

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.LineHeightStyle
import coil3.compose.AsyncImage
import com.example.photobasedtextrpg.R
import com.example.photobasedtextrpg.ui.components.Genre
import com.example.photobasedtextrpg.ui.components.WoodButton
import com.example.photobasedtextrpg.ui.components.dropShadow
import com.example.photobasedtextrpg.ui.theme.PhotoBasedTextRPGTheme


private val EndingTextBrush = Brush.verticalGradient(
  colorStops = arrayOf(
    0.0f to Color(0xFFFFF5E1),
    0.7f to Color(0xFFD5B586),
    1.0f to Color(0xFFBA996A)
  )
)

sealed interface EndingUiState {
  data object Loading : EndingUiState
  data class Success(
    val storyTitle: String,
    val imageUri: String,
    val genre: Genre,         // ジャンルを追加
    val summary: String       // Nano Summarization APIが生成した3行テキスト
  ) : EndingUiState

  data class Error(val message: String) : EndingUiState
}

@Composable
fun EndingScreen(
  uiState: EndingUiState,
  onShare: () -> Unit,
  onPlayAgain: () -> Unit
) {


  Box(modifier = Modifier.fillMaxSize()) {
    // 背景画像の設定
    if (uiState is EndingUiState.Success) {
      val backgroundRes = when (uiState.genre) {
        Genre.SF -> R.drawable.background_sf
        Genre.HORROR -> R.drawable.background_horror
        Genre.MYSTERY -> R.drawable.background_mistery
        else -> R.drawable.backgorund_fantasy
      }
      Image(
        painter = painterResource(backgroundRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    } else {
      Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White))
    }



    when (uiState) {
      is EndingUiState.Loading -> {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
      }

      is EndingUiState.Error -> {
        Text(
          text = uiState.message,
          color = Color.Red,
          modifier = Modifier.align(Alignment.Center)
        )
      }

      is EndingUiState.Success -> {

        Column(modifier = Modifier.fillMaxSize()) {
          // 1. 上部ヘッダ (GameScreen と同様)
          Box(modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(elevation = 36.dp, RectangleShape)) {
            Image(
              painter = painterResource(R.drawable.banner_middle),
              contentDescription = null,
              contentScale = ContentScale.FillBounds,
              modifier = Modifier
                .fillMaxSize()
                .dropShadow(
                  color = Color.Black.copy(alpha = 0.6f),
                  borderRadius = 0.dp,
                  blurRadius = 16.dp,
                  offsetY = 6.dp
                )
            )
            Text(
              text = "冒険の終わり",
              color = Color.White,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily(
                Font(
                  R.font.noto_sans_jp_bold,
                  FontWeight.Bold
                )
              ),
              modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp)
            )
          }
          Column(
            modifier = Modifier.weight(1f)
              .fillMaxSize()
              .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {


            // 2. 写真サムネイル
            AsyncImage(
              model = uiState.imageUri,
              contentDescription = null,
              contentScale = ContentScale.Fit,
              modifier = Modifier
                .fillMaxSize()
                .height(220.dp)
            )
            Box() {
              Text(
                text = uiState.storyTitle,
                color = Color.Black,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(
                  Font(
                    R.font.noto_sans_jp_bold,
                    FontWeight.Bold
                  )
                ),
                modifier = Modifier
                  .padding(start = 24.dp),
                style = TextStyle(
                  shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(5.0f, 10.0f),
                    blurRadius = 12f
                  )
                )

              )
              Text(
                text = uiState.storyTitle,
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(
                  Font(
                    R.font.noto_sans_jp_bold,
                    FontWeight.Bold
                  )
                ),
                modifier = Modifier
                  .padding(start = 24.dp),
                style = TextStyle(
                  brush = EndingTextBrush
                )

              )
            }

            // 3. あらすじテキスト領域（old_paper_medium を背景に）
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(
                  start = 16.dp,
                  top = 16.dp,
                  end = 16.dp,
                  bottom = 0.dp
                )
            ) {
              Image(
                painter = painterResource(R.drawable.old_paper_large),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                //modifier = Modifier.matchParentSize()
              )
              Text(
                text = uiState.summary,
                modifier = Modifier.padding(24.dp),
                fontSize = 30.sp,
                color = Color.Black,
                style = LocalTextStyle.current.merge(
                  TextStyle(
                    lineHeight = 1.2.em,
                    platformStyle = PlatformTextStyle(
                      includeFontPadding = false
                    ),
                    lineHeightStyle = LineHeightStyle(
                      alignment = LineHeightStyle.Alignment.Center,
                      trim = LineHeightStyle.Trim.None
                    )
                  )
                ),
              )
            }

            // 下部に余白を作る（ボタン領域と重ならないように）
            Spacer(modifier = Modifier.height(180.dp))
          }
          // 4. 下部領域（ending_bottom を背景に）を Box の下部に固定
          Box(
            modifier = Modifier
              .fillMaxWidth()

          ) {
            Image(
              painter = painterResource(R.drawable.ending_bottom),
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
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 24.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              // SNSシェアボタン（share_story画像）
              Image(
                painter = painterResource(R.drawable.share_story),
                contentDescription = "物語をシェア",
                modifier = Modifier
                  .fillMaxWidth(0.7f)
                  .clickable { onShare() }
              )
              // もう一度ボタン
              WoodButton(
                text = "もう一度冒険に出る",
                onClick = onPlayAgain,
                modifier = Modifier
                  .fillMaxWidth(0.85f)
                  .height(52.dp)
              )
            }
          }

        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun EndingScreenSuccessPreview() {
  PhotoBasedTextRPGTheme(
    content = {
      EndingScreen(
        uiState = EndingUiState.Success(
          storyTitle = "霧の谷の遺構あなたは古城の謎を解き明かした",
          imageUri = "",
          genre = Genre.FANTASY,
          summary = "• あなたは古城の謎を解き明かした\n• 失われた宝を見つけ出した\n• 霧の谷に平和が戻った"
        ),
        onShare = {},
        onPlayAgain = {}
      )
    },
  )
}
