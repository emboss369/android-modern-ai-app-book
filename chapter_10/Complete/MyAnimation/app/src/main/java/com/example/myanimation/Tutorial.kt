package com.example.myanimation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Tutorial(
  modifier: Modifier = Modifier,
  @DrawableRes imageRes: Int,
  title: String,
  description: String,
  pageOffset: Float = 0f
) {
  Box(
    modifier = modifier.fillMaxSize()
  ) {
    Image(
      modifier = Modifier.fillMaxSize(),
      painter = painterResource(id = imageRes),
      contentScale = ContentScale.Crop,
      contentDescription = description
    )
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 50.dp, vertical = 60.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        modifier = Modifier.graphicsLayer {
          alpha = 1f - pageOffset
          translationY = (pageOffset * 300).dp.toPx()
        },
        text = title,
        fontFamily = FontFamily.Cursive,
        fontSize = 60.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White,
        lineHeight = 50.sp,
        style = TextStyle(
          shadow = Shadow(
            color = Color.Black.copy(alpha = 0.5f),
            blurRadius = 10f, offset = Offset(4f, 4f)
          )
        )
      )
      Spacer(modifier = Modifier.size(40.dp))
      Text(
        modifier = Modifier.graphicsLayer {
          alpha = 1f - pageOffset
          translationX = (pageOffset * 300).dp.toPx()
        },
        text = description,
        fontFamily = FontFamily.SansSerif,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        lineHeight = 32.sp,
        style = TextStyle(
          shadow = Shadow(
            color = Color.Black.copy(alpha = 0.5f),
            blurRadius = 8f, offset = Offset(2f, 2f)
          )
        )
      )
    }
  }
}

@Preview
@Composable
fun TutorialPreview() {
  Tutorial(
    imageRes = R.drawable.tutorial0,
    title = "Discover Fashion Trends",
    description = "「ファッショントレンドを発見しよう」\nこのアプリでは、最新のファッショントレンドやスタイリングのアイデアを簡単に見つけることができます。洗練された写真やインスピレーションに満ちたコンテンツがあなたを待っています。",
    pageOffset = 0f
  )
}