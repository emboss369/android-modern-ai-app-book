package com.example.postmystory

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.postmystory.ui.theme.PostMyStoryTheme

@Composable
fun ArticleView(
  message: Message,
  modifier: Modifier = Modifier
) {
  var expanded by remember { mutableStateOf(false) }

  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = Color.White)
  ) {
    Column {
      AsyncImage(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
          .padding(16.dp)
          .clip(MaterialTheme.shapes.medium),
        model = ImageRequest.Builder(LocalContext.current)
          .data(message.image)
          .crossfade(true)
          .diskCachePolicy(CachePolicy.DISABLED)
          .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(id = R.drawable.now_loading)
      )

      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          painter = painterResource(R.drawable.thumb_up_24px),
          contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "「Nice!」 ${message.nice}件"
        )
      }

      Text(
        text = message.caption,
        maxLines = if (expanded) Int.MAX_VALUE else 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .padding(bottom = 16.dp)
          .animateContentSize()
          .clickable { expanded = !expanded }
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ArticleViewPreview() {
  PostMyStoryTheme {
    ArticleView(
      Message(
        caption = "Hello Android".repeat(20),
        image = "https://smartphone-zine.com/wp-content/uploads/2026/03/Android_Robot.png",
        nice = 999
      ),
      modifier = Modifier.padding(16.dp)
    )
  }
}