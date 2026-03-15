package com.example.myanimation.sample

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myanimation.ui.theme.MyAnimationTheme
import kotlinx.coroutines.launch

@Composable
fun AnimatableView(modifier: Modifier = Modifier) {
  var isOk by remember { mutableStateOf(true) }
  val color = remember { Animatable(Color.Gray) }

  LaunchedEffect(isOk) {
    color.animateTo(
      targetValue = if (isOk) Color.Green else Color.Red,
      animationSpec = tween(durationMillis = 1000)
    )
  }
  val composableScope = rememberCoroutineScope()

  Column(modifier = modifier.padding(16.dp)) {
    Box(
      modifier = Modifier
        .size(200.dp)
        .background(color.value)
    )
    Button(
      modifier = Modifier.padding(top = 16.dp),
      onClick = { isOk = !isOk }
    ) {
      Text("ok: $isOk")
    }
    Button(
      modifier = Modifier.padding(top = 8.dp),
      onClick = {
        composableScope.launch {
          color.snapTo(targetValue = Color.Magenta)
        }
      }
    ) {
      Text("snapTo Magenta")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun AnimatableViewPreview() {
  MyAnimationTheme {
    AnimatableView()
  }
}