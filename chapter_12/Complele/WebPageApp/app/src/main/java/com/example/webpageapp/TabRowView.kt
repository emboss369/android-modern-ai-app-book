package com.example.webpageapp

import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TabRowView(tabIndex: Int, onTabChange: (Int) -> Unit) {
  val tabs = listOf(
    "Home" to ImageVector.vectorResource(id = R.drawable.home_24px),
    "About" to ImageVector.vectorResource(id = R.drawable.info_24px),
    "Settings" to ImageVector.vectorResource(id = R.drawable.settings_24px))

  TabRow(selectedTabIndex = tabIndex) {
    tabs.forEachIndexed { index, tab ->
      Tab(selected = tabIndex == index,
        onClick = { onTabChange(index) },
        text = { Text(tab.first) },
        icon = {
          Icon(imageVector = tab.second, contentDescription = tab.first)
        })
    }
  }
}

@Preview
@Composable
fun TabRowViewPreview() {
  var index by remember { mutableStateOf(0) }
  TabRowView(tabIndex = index, onTabChange = { index = it })
}