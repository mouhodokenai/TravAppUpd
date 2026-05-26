package com.example.travappupd.data.model.repository

import com.example.travappupd.data.entities.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    val allTrips: Flow<List<Trip>>

    // Операции
    suspend fun getTripById(id: Long): Flow<Trip?>
    suspend fun insertTrip(trip: Trip): Long
    suspend fun updateTrip(trip: Trip)
    suspend fun deleteTrip(trip: Trip)
    suspend fun deleteAllTrips()
}