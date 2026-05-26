package com.example.travappupd.data.model.repository

import com.example.travappupd.data.dao.TripDao
import com.example.travappupd.data.entities.Trip
import kotlinx.coroutines.flow.Flow

class TripRepositoryImpl(private val tripDao: TripDao) : TripRepository {

    override val allTrips: Flow<List<Trip>> = tripDao.getAllTrips()

    override suspend fun getTripById(id: Long): Flow<Trip?> {
        return tripDao.getByTripId(id)
    }

    override suspend fun insertTrip(trip: Trip): Long {
        return tripDao.insert(trip)
    }

    override suspend fun updateTrip(trip: Trip) {
        tripDao.update(trip)
    }

    override suspend fun deleteTrip(trip: Trip) {
        tripDao.delete(trip)
    }

    override suspend fun deleteAllTrips() {
        tripDao.deleteAllTrips()
    }

}