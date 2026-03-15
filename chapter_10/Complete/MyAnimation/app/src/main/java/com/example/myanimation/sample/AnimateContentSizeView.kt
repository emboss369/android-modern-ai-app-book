package com.example.myanimation.sample

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun AnimateContentSizeView(modifier: Modifier = Modifier) {
  var flag by remember { mutableStateOf(true) }

  Column(modifier = modifier.width(300.dp).padding(16.dp)) {
    Text(
      modifier = Modifier
        .background(Color.LightGray)
        .animateContentSize(),
      text = if (flag) "Hello" else "Hello Compose Happy Kotlin coding"
    )
    Text(
      modifier = Modifier
        .padding(top = 16.dp)
        .background(Color.Yellow),
      text = if (flag) "Hello (No Anim)" else "Hello Compose Happy Kotlin coding (No Anim)"
    )
    Button(
      modifier = Modifier.padding(top = 16.dp),
      onClick = { flag = !flag }
    ) {
      Text(text = "Toggle message")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun AnimateContentSizeViewPreview() {
  MyAnimationTheme {
    AnimateContentSizeView()
  }
}