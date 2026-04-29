package com.example.mytodo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MockItemsRepository : ItemsRepository {
    private val _items = MutableStateFlow(
        listOf(
            Item(1, "バルコニーの植物に水をやる", "", false),
            Item(2, "オンラインコースの申し込みをする", "", false),
            Item(3, "水道の滴り漏れを修理する", "", true),
            Item(4, "友人の誕生日プレゼントを選ぶ", "", false),
            Item(5, "電球を交換する", "", true),
            Item(6, "週末の家族旅行の計画を立てる", "", false),
            Item(7, "牛乳を買う", "", false),
            Item(8, "タイムマシンの修理", "", true)
        )
    )

    override fun getAllItemsStream(): Flow<List<Item>> = _items.asStateFlow()

    override fun getItemStream(id: Int): Flow<Item?> =
        _items.asStateFlow().map { it.find { item -> item.id == id } }

    override suspend fun insertItem(item: Item) {
        _items.update { it + item.copy(id = (it.maxOfOrNull { i -> i.id } ?: 0) + 1) }
    }

    override suspend fun deleteItem(item: Item) {
        _items.update { it.filterNot { i -> i.id == item.id } }
    }

    override suspend fun updateItem(item: Item) {
        _items.update { it.map { i -> if (i.id == item.id) item else i } }
    }
}
