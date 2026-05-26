package com.example.travappupd.data.model.repository

import com.example.travappupd.data.dao.RouteDao
import com.example.travappupd.data.entities.Route
import kotlinx.coroutines.flow.Flow

class RouteRepository(private val routeDao: RouteDao) : ItemRepository<Route> {

    override fun getItemsByTripId(tripId: Long): Flow<List<Route>> {
        return routeDao.getByTripId(tripId)
    }

    override suspend fun insertItem(item: Route): Long {
        return routeDao.insert(item)
    }

    override suspend fun updateItem(item: Route) {
        return routeDao.update(item)
    }

    override suspend fun deleteItem(item: Route) {
        return routeDao.delete(item)
    }

    override suspend fun deleteAllItems() {
        return routeDao.deleteAllRoutes()
    }
}