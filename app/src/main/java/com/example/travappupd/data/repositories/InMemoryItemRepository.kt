package com.example.travappupd.data.repositories

import com.example.travappupd.data.entities.generateLocalId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class InMemoryItemRepository<Item>(
    private val getId: (Item) -> Long,
    private val withId: (Item, Long) -> Item,
    private val getTripId: (Item) -> Long
) : ItemRepository<Item> {

    private val _items = MutableStateFlow<List<Item>>(emptyList())

    override fun getItemsByTripId(tripId: Long): Flow<List<Item>> =
        _items.map { list -> list.filter { getTripId(it) == tripId } }

    override suspend fun insertItem(item: Item): Long {
        val id = getId(item).takeIf { it != 0L } ?: generateLocalId()
        _items.update { it + withId(item, id) }
        return id
    }

    override suspend fun updateItem(item: Item) {
        _items.update { list -> list.map { if (getId(it) == getId(item)) item else it } }
    }

    override suspend fun deleteItem(item: Item) {
        _items.update { list -> list.filterNot { getId(it) == getId(item) } }
    }

    override suspend fun deleteAllItems() { _items.value = emptyList() }

    fun getAllItems(): List<Item> = _items.value
}