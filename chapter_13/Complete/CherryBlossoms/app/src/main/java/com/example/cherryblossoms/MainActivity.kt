package com.example.cherryblossoms

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.example.cherryblossoms.ui.theme.CherryBlossomsTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalFoundationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      CherryBlossomsTheme {
        val cherryList = remember {
          getCherryList(getJson(resources))
        }
        Scaffold(
          modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
          val titles = listOf("桜100選", "Wikipedia")
          val pagerState = rememberPagerState(pageCount = { titles.size })
          val scope = rememberCoroutineScope()
          Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(
              selectedTabIndex = pagerState.currentPage
            ) {
              titles.forEachIndexed { index, title ->
                Tab(selected = pagerState.currentPage == index, onClick = {
                  scope.launch {
                    pagerState.animateScrollToPage(index)
                  }
                }, text = { Text(title) })
              }
            }
            HorizontalPager(state = pagerState) { page ->
              when (page) {
                0 -> CherryList(cherryList) { cherry ->
                  openGoogleMaps( Pair(cherry.latitude, cherry.longitude))
                }
                1 -> WikipediaView { search ->
                  val url = "https://ja.wikipedia.org/wiki/${search.title}"
                  openBrowser(url)
                }
              }
            }
          }
        }
      }
    }
  }

  fun getJson(resources: Resources): String {
    Log.d("FILE OPEN", "100Cherry_List.json")
    val inputStream = resources.assets.open("100Cherry_List.json")
    inputStream.use { inputStream ->
      inputStream.bufferedReader().use { bufferedReader ->
        return bufferedReader.readText()
      }
    }
  }

  fun getCherryList(str: String): List<Cherry> {
    val obj = Json.decodeFromString<List<Cherry>>(str)
    return obj
  }

  fun openGoogleMaps(coordinates: Pair<String, String>) {
    val gmmIntentUri = "geo:${coordinates.first},${coordinates.second}".toUri()
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(mapIntent)
  }

  fun openBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    startActivity(intent)
  }
}
