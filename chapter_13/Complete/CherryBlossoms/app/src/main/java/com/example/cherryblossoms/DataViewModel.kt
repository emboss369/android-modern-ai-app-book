package com.example.cherryblossoms

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cherryblossoms.data.ApiResult
import com.example.cherryblossoms.data.ApiSearch
import fuel.Fuel
import fuel.get
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.io.readString

class DataViewModel : ViewModel() {
  var cherryList =  mutableStateListOf<ApiSearch>()

  fun query() {
    viewModelScope.launch {
      try {
        val url = """
          https://ja.wikipedia.org/w/api.php?
          action=query&format=json&prop=images&list=search&formatversion=2&
          imlimit=1&srsearch=桜の名所&srlimit=500
          """.trimIndent().replace(System.lineSeparator(), "")
        
        val response = Fuel.get(
          url = url,
          headers = mapOf("User-Agent" to "CherryBlossoms/1.0 (https://example.com/cherryblossoms; contact@example.com)")
        )

        val string = response.source.readString()
        val json = Json { ignoreUnknownKeys = true }
        val result = json.decodeFromString<ApiResult>(string)
        cherryList.apply {
          clear()
          addAll(result.query.search)
        }
      } catch (e: Exception) {
        Log.e("DataViewModel", "Error during API query or JSON decoding", e)
      }
    }
  }
}
