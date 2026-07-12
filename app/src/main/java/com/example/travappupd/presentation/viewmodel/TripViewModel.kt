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
import com.example.travappupd.data.model.repository.ItemRepository
import com.example.travappupd.data.repositories.DraftTripRepository
import com.example.travappupd.data.repositories.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import com.example.travappupd.data.entities.generateLocalId
import com.example.travappupd.data.repositories.InMemoryItemRepository
import kotlinx.coroutines.flow.update


@HiltViewModel
open class TripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    val tripId: Long = generateLocalId()

    private val _tripName = MutableStateFlow("")
    val tripName: StateFlow<String> = _tripName.asStateFlow()
    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate: StateFlow<LocalDate?> = _startDate.asStateFlow()
    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate: StateFlow<LocalDate?> = _endDate.asStateFlow()

    val budgetRepository = InMemoryItemRepository<Budget>(
        getId = { it.budgetId },
        withId = { i, id -> i.copy(budgetId = id) },
        getTripId = { it.tripId }
    )
    val hotelRepository = InMemoryItemRepository<Hotel>(
        getId = { it.hotelId }, withId = { i, id -> i.copy(hotelId = id) }, getTripId = { it.tripId }
    )
    val noteRepository = InMemoryItemRepository<Note>(
        getId = { it.noteId }, withId = { i, id -> i.copy(noteId = id) }, getTripId = { it.tripId }
    )
    val packingListRepository = InMemoryItemRepository<PackingList>(
        getId = { it.itemId }, withId = { i, id -> i.copy(itemId = id) }, getTripId = { it.tripId }
    )
    val routeRepository = InMemoryItemRepository<Route>(
        getId = { it.routeId }, withId = { i, id -> i.copy(routeId = id) }, getTripId = { it.tripId }
    )
    val ticketRepository = InMemoryItemRepository<Ticket>(
        getId = { it.ticketId }, withId = { i, id -> i.copy(ticketId = id) }, getTripId = { it.tripId }
    )

    fun updateTripName(newName: String) { _tripName.value = newName }
    fun updateStartDate(newDate: LocalDate?) { _startDate.value = newDate }
    fun updateEndDate(newDate: LocalDate?) { _endDate.value = newDate }

    fun saveTrip() {
        viewModelScope.launch {
            val trip = Trip(
                tripId = tripId,
                title = _tripName.value.ifEmpty { "Новая поездка" },
                startDate = _startDate.value,
                endDate = _endDate.value
            )
            repository.saveTripWithChildren(
                trip = trip,
                budgets = budgetRepository.getAllItems(),
                hotels = hotelRepository.getAllItems(),
                notes = noteRepository.getAllItems(),
                packingItems = packingListRepository.getAllItems(),
                routes = routeRepository.getAllItems(),
                tickets = ticketRepository.getAllItems()
            )
        }
    }
}

class PreviewTripViewModel() : TripViewModel(
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