package com.example.travappupd.data.model.repository

import kotlinx.coroutines.flow.Flow

interface ItemRepository<Item> {

    fun getItemsByTripId(tripId: Long): Flow<List<Item>>

    suspend fun insertItem(item: Item): Long
    suspend fun updateItem(item: Item)
    suspend fun deleteItem(item: Item)
    suspend fun deleteAllItems()
}