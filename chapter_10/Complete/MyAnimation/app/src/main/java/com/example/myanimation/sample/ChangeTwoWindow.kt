package com.example.myanimation.sample

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myanimation.ui.theme.MyAnimationTheme

@Composable
fun ChangeTwoWindow(modifier: Modifier = Modifier) {
  var isBlue by remember { mutableStateOf(true) }
  val backgroundColor by animateColorAsState(
    targetValue = if (isBlue) Color.Blue else Color.Yellow,
    label = "background color animation"
  )
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(backgroundColor)
      .padding(16.dp)
  ) {
    Button(onClick = { isBlue = !isBlue }) {
      Text("背景切り替え")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ChangeTwoWindowPreview() {
  MyAnimationTheme {
    ChangeTwoWindow()
  }
}