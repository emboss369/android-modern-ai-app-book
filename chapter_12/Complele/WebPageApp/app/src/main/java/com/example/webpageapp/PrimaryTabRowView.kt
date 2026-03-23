package com.example.webpageapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTabRowView(tabIndex: Int, onTabChange: (Int) -> Unit) {

  val tabs = listOf(
    "Home" to ImageVector.vectorResource(id = R.drawable.home_24px),
    "About" to ImageVector.vectorResource(id = R.drawable.info_24px),
    "Settings" to ImageVector.vectorResource(id = R.drawable.settings_24px)
  )

  PrimaryTabRow(selectedTabIndex = tabIndex) {
    tabs.forEachIndexed { index, tab ->
      Tab(text = { Text(tab.first) },
        selected = tabIndex == index,
        onClick = { onTabChange(index) },
        icon = {
          Icon(
            imageVector = tab.second, contentDescription = tab.first
          )
        })
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PrimaryTabRowViewPreview() {
  var index by remember { mutableStateOf(0) }
  PrimaryTabRowView(tabIndex = index, onTabChange = { index = it })
}