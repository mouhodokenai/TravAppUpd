package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.entities.Hotel
import com.example.travappupd.data.entities.Note
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.data.entities.Trip
import com.example.travappupd.data.repositories.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
open class TripsViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val trips: StateFlow<List<Trip>> = combine(
        repository.allTrips,
        _searchQuery
    ) { allTrips, query ->
        if (query.isBlank()) {
            allTrips
        } else {
            allTrips.filter { it.title.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

}

class PreviewTripsViewModel : TripsViewModel(
    repository = object : TripRepository {
        override val allTrips = flowOf(
            listOf(
                Trip(tripId = 1, title = "Рим", startDate = null, endDate = null),
                Trip(tripId = 2, title = "Париж", startDate = null, endDate = null),
                Trip(tripId = 3, title = "Лондон", startDate = null, endDate = null)
            )
        )

        override fun getTripById(id: Long): Flow<Trip?> =
            allTrips.map { it.find { trip -> trip.tripId == id } }

        override suspend fun insertTrip(trip: Trip): Long = 1L
        override suspend fun updateTrip(trip: Trip) {}
        override suspend fun deleteTrip(trip: Trip) {}
        override suspend fun deleteAllTrips() {}
        override suspend fun deleteTripById(id: Long) {}

        override suspend fun saveTripWithChildren(
            trip: Trip,
            budgets: List<Budget>,
            hotels: List<Hotel>,
            notes: List<Note>,
            packingItems: List<PackingList>,
            routes: List<Route>,
            tickets: List<Ticket>
        ) {
        }
    }
)