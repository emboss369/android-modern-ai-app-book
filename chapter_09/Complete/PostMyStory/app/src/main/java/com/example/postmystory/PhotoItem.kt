package com.example.postmystory

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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

@Composable
fun PhotoItem(
  photo: String,
  onClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(photo)
      .crossfade(true)
      .diskCachePolicy(CachePolicy.DISABLED)
      .build(),
    contentDescription = null,
    modifier = modifier
      .aspectRatio(1f)
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick(photo) },
    contentScale = ContentScale.Crop,
    placeholder = painterResource(id = R.drawable.now_loading),
    onLoading = { Log.d("PhotoItem", "Loading: $photo") },
    onSuccess = { Log.d("PhotoItem", "Success: $photo") },
    onError = { error ->
      Log.e("PhotoItem", "Error: $photo", error.result.throwable) }
  )
}

@Preview
@Composable
fun PhotoItemPreview() {
  PhotoItem(photo = "https://picsum.photos/200/200",
    onClick = { })
}