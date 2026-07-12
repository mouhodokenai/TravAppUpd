package com.example.travappupd.data.repositories

import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.entities.Hotel
import com.example.travappupd.data.entities.Note
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.data.entities.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    val allTrips: Flow<List<Trip>>

    fun getTripById(id: Long): Flow<Trip?>
    suspend fun insertTrip(trip: Trip): Long
    suspend fun updateTrip(trip: Trip)
    suspend fun deleteTrip(trip: Trip)
    suspend fun deleteAllTrips()
    suspend fun deleteTripById(id: Long)

    suspend fun saveTripWithChildren(
        trip: Trip,
        budgets: List<Budget>,
        hotels: List<Hotel>,
        notes: List<Note>,
        packingItems: List<PackingList>,
        routes: List<Route>,
        tickets: List<Ticket>
    )


}