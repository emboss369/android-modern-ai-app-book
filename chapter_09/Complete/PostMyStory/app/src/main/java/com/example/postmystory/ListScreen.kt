package com.example.postmystory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ListScreen(
  messages: List<Message>,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    modifier = modifier,
    floatingActionButton = {
      FloatingActionButton(onClick = onClick) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add a new post"
        )
      }
    }
  ) { padding ->
    if (messages.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(padding),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.LightGray
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "まだ投稿がありません\n右下のボタンから最初の1枚を投稿しましょう！",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(items = messages) { message ->   // ❹
          ArticleView(message = message)
        }
      }
    }
  }
}
@Preview
@Composable
fun ListScreenPreviewNoData() {
  val messages = mutableListOf<Message>()
  ListScreen(messages = messages, onClick = { })
}
@Preview
@Composable
fun ListScreenPreview() {
  val messages = mutableListOf<Message>().apply {
    for (i in 1..24) {
      add(
        Message(
          image = "https://picsum.photos/seed/$i/200",
          caption = "Caption $i",
          nice = i
        )
      )
    }
  }
  ListScreen(messages = messages, onClick = { })
}