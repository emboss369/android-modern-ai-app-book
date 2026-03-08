package com.example.postmystory

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.postmystory.ui.theme.PostMyStoryTheme

@Composable
fun PhotoGridScreen(
  onClick: (String) -> Unit,   // ❶
  modifier: Modifier = Modifier
) {
  val photos = remember {
    List(300) { "https://picsum.photos/seed/$it/200/200" }   // ❷
  }
  LazyVerticalGrid(
    modifier = modifier.fillMaxSize()
      .safeDrawingPadding(),
    columns = GridCells.Adaptive(minSize = 128.dp),   // ❸
    contentPadding = PaddingValues(8.dp)
  ) {
    items(items = photos) { photo ->
      PhotoItem(
        photo = photo,
        onClick = onClick,
        modifier = Modifier
          .padding(4.dp)
          .fillMaxSize()
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PhotoGridScreenPreview() {
  PostMyStoryTheme {
    PhotoGridScreen(onClick = { })
  }
}