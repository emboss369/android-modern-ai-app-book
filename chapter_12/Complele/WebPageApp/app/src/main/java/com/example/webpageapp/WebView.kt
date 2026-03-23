package com.example.webpageapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebView(url: String, modifier: Modifier = Modifier) {
  AndroidView(
    modifier = modifier,
    factory = { context ->
      android.webkit.WebView(context).apply {
        webChromeClient = android.webkit.WebChromeClient()
        webViewClient = android.webkit.WebViewClient()
        settings.javaScriptEnabled = true
        loadUrl(url)
      }
    }
  )
}
