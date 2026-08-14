package com.example.travappupd.data.model.repository

import com.example.travappupd.data.dao.PackingListDao
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.data.repositories.ItemRepository
import kotlinx.coroutines.flow.Flow

class PackingListRepository(private val packingListDao: PackingListDao) :
    ItemRepository<PackingList> {

    override fun getItemsByTripId(tripId: Long): Flow<List<PackingList>> {
        return packingListDao.getByTripId(tripId)
    }

    override suspend fun insertItem(item: PackingList): Long {
        return packingListDao.insert(item)
    }

    override suspend fun updateItem(item: PackingList) {
        return packingListDao.update(item)
    }

    override suspend fun deleteItem(item: PackingList) {
        return packingListDao.delete(item)
    }

    override suspend fun deleteAllItems() {
        return packingListDao.deleteAllPackingItems()
    }
}

