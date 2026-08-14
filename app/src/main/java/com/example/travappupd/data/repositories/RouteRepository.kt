package com.example.travappupd.data.repositories

import com.example.travappupd.data.dao.RouteDao
import com.example.travappupd.data.entities.Route
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val routeDao: RouteDao
) : ItemRepository<Route> {

    override fun getItemsByTripId(tripId: Long): Flow<List<Route>> =
        routeDao.getByTripId(tripId)

    override suspend fun insertItem(item: Route): Long {
        return routeDao.insert(item)
    }

    override suspend fun updateItem(item: Route) {
        routeDao.update(item)
    }

    override suspend fun deleteItem(item: Route) {
        routeDao.delete(item)
    }

    override suspend fun deleteAllItems() {
        routeDao.deleteAll()
    }

}