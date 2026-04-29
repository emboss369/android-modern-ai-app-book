package com.example.mytodo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mytodo.R
import com.example.mytodo.data.Item

@Composable
fun TodoItem(
  item: Item, modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Box(contentAlignment = Alignment.Center) {
      Column(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(
          dimensionResource(id = R.dimen.padding_small)
        )
      ) {
        Row(modifier = Modifier.fillMaxWidth()) {
          Text(text = item.title, style = MaterialTheme.typography.titleLarge)
          Spacer(Modifier.weight(1f))
          Text(
            text = if (item.done) {
              stringResource(R.string.completed)
            } else {
              stringResource(R.string.not_yet)
            },
            style = MaterialTheme.typography.titleMedium
          )
        }
      }
      if (item.done) {
        HorizontalDivider(
          modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}

@Preview()
@Composable
fun TodoItemPreview() {
  TodoItem(item = Item(1, "タイトル", "詳細", true), modifier = Modifier.padding(16.dp))
}