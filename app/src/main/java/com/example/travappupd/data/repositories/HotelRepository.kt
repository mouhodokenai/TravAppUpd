package com.example.travappupd.data.repositories

import com.example.travappupd.data.dao.HotelDao
import com.example.travappupd.data.entities.Hotel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HotelRepository @Inject constructor(
    private val hotelDao: HotelDao
) : ItemRepository<Hotel> {

    override fun getItemsByTripId(tripId: Long): Flow<List<Hotel>> {
        return hotelDao.getHotelsForTrip(tripId)
    }

    override suspend fun insertItem(item: Hotel): Long {
        return hotelDao.insert(item)
    }

    override suspend fun updateItem(item: Hotel) {
        hotelDao.update(item)
    }

    override suspend fun deleteItem(item: Hotel) {
        hotelDao.delete(item)
    }

    override suspend fun deleteAllItems() {
        hotelDao.deleteAllHotels()
    }
}