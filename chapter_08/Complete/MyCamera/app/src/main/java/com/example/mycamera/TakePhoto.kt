package com.example.mycamera

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TakePhoto(modifier: Modifier = Modifier, onClick: () -> Unit) {
  IconButton( // ❶
    onClick = onClick,
    modifier = modifier
      .size(80.dp)
      .border(5.dp, Color.White, CircleShape) // ❷
      .padding(8.dp)
      .background(Color.Red, CircleShape) // ❸
  ) {
    Box(modifier = Modifier.fillMaxSize())
  }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TakePhotoPreview() {
  TakePhoto(onClick = {})
}