package com.example.webpageapp

import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
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

@Composable
fun ScrollableTabRowView(tabIndex: Int, onTabChange: (Int) -> Unit) {
  val tabs = listOf(
    "Home" to ImageVector.vectorResource(id = R.drawable.home_24px),
    "About" to ImageVector.vectorResource(id = R.drawable.info_24px),
    "Settings" to ImageVector.vectorResource(id = R.drawable.settings_24px),
    "User" to ImageVector.vectorResource(id = R.drawable.person_24px),
    "Nice" to ImageVector.vectorResource(id = R.drawable.thumb_up_24px),
    "Email" to ImageVector.vectorResource(id = R.drawable.mail_24px),
    "Star" to ImageVector.vectorResource(id = R.drawable.star_24px),
    "Menu" to ImageVector.vectorResource(id = R.drawable.menu_24px)
  )
  ScrollableTabRow(selectedTabIndex = tabIndex) {
    tabs.forEachIndexed { index, (title, icon) ->
      Tab(
        selected = tabIndex == index,
        onClick = { onTabChange(index) },
        text = { Text(title) },
        icon = { Icon(imageVector = icon, contentDescription = title) }
      )
    }
  }
}

@Preview
@Composable
fun ScrollableTabRowViewPreview() {
  var index by remember { mutableStateOf(0) }
  ScrollableTabRowView(tabIndex = index, onTabChange = { index = it })
}