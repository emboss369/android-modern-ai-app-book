package com.example.postmystory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.postmystory.ui.theme.PostMyStoryTheme

@Composable
fun CaptionScreen(
  selectUrl: String,
  onClick: () -> Unit,
  onChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var text by remember { mutableStateOf("") }
  val scrollState = rememberScrollState()

  Scaffold(
    modifier = modifier
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(scrollState)
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      AsyncImage(
        modifier = Modifier
          .size(200.dp)
          .aspectRatio(1f)
          .clip(MaterialTheme.shapes.medium),
        model = ImageRequest.Builder(LocalContext.current)
          .data(selectUrl)
          .crossfade(true)
          .diskCachePolicy(CachePolicy.DISABLED)
          .build(),
        placeholder = painterResource(id = R.drawable.now_loading),
        contentDescription = null,
        contentScale = ContentScale.Crop
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = { newText ->
          text = newText
          onChange(newText)
        },
        label = { Text(text = "キャプションを入力してください") },
        minLines = 3,
        maxLines = 10
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(text = "投稿する")
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun CaptionScreenPreview() {
  PostMyStoryTheme {
    CaptionScreen(
      selectUrl = "https://picsum.photos/200/200",
      onClick = { },
      onChange = { }
    )
  }
}