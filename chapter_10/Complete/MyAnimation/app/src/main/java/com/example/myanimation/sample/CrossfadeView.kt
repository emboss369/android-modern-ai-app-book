package com.example.myanimation.sample

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myanimation.R

@Composable
fun CrossfadeView(modifier: Modifier = Modifier) {
  var currentPage by remember { mutableStateOf("A") }

  Column(modifier = modifier.padding(16.dp)) {
    Crossfade(
      targetState = currentPage,
      label = "crossfade animation"
    ) { screen ->
      when (screen) {
        "A" -> Image(
          modifier = Modifier.size(200.dp),
          painter = painterResource(id = R.drawable.slide04),
          contentDescription = null
        )
        "B" -> Image(
          modifier = Modifier.size(200.dp),
          painter = painterResource(id = R.drawable.slide09),
          contentDescription = null
        )
      }
    }
    Button(
      modifier = Modifier.padding(top = 16.dp),
      onClick = { currentPage = if (currentPage == "A") "B" else "A" }
    ) {
      Text(text = "currentPage: $currentPage")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun CrossfadeViewPreview() {
  CrossfadeView()
}