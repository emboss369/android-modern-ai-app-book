package com.example.postmystory

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.example.postmystory.ui.theme.PostMyStoryTheme

class MainActivity : ComponentActivity() {
  enum class Scene {   // ❶
    LIST, PHOTOS, CAPTION
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val messages = remember { mutableStateListOf<Message>() }   // ❷
      var currentScene by remember { mutableStateOf(Scene.LIST) }   // ❸
      var selectedImageUrl by remember { mutableStateOf("") }
      var currentCaption by remember { mutableStateOf("") }

      PostMyStoryTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = Color.White
        ) {
          when (currentScene) {
            Scene.LIST -> {
              ListScreen(
                messages = messages,
                onClick = { currentScene = Scene.PHOTOS },   // ❹
                modifier = Modifier.fillMaxSize()
              )
            }

            Scene.PHOTOS -> {
              PhotoGridScreen(
                onClick = { url ->
                  selectedImageUrl = url   // ❻
                  currentScene = Scene.CAPTION   // ❺
                },
                modifier = Modifier.fillMaxSize()
              )
            }

            Scene.CAPTION -> {
              CaptionScreen(
                selectUrl = selectedImageUrl,
                onChange = { currentCaption = it },
                onClick = {
                  messages.add(   // ❼
                    index = 0,
                    element = Message(
                      image = selectedImageUrl,
                      caption = currentCaption,
                      nice = 0
                    )
                  )
                  currentScene = Scene.LIST   // ❽
                  currentCaption = ""
                },
                modifier = Modifier.fillMaxSize()
              )
            }
          }
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
  PostMyStoryTheme {
    Greeting("Android")
  }
}

@Composable
fun CoilTest() {
  AsyncImage(
    model =
      "https://smartphone-zine.com/wp-content/uploads/2026/03/Android_Robot.png",
    contentDescription = null,
  )
}

@Preview
@Composable
fun CoilTestPreview() {
  CoilTest()
}