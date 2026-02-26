package com.example.order

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.order.ui.theme.LightGreen100
import com.example.order.ui.theme.OrderTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      OrderTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = LightGreen100  // ❶
        ) {
          MainScreen()
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  OrderTheme {
    Greeting("Android")
  }
}