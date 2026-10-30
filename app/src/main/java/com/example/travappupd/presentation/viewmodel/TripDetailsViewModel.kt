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
import com.example.travappupd.data.repositories.GeocodingRepository
import com.example.travappupd.data.repositories.GeocodingResult
import com.example.travappupd.data.repositories.ItemRepository
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
import kotlin.text.ifEmpty


@HiltViewModel
open class TripDetailsViewModel @Inject constructor(
    private val repository: TripRepository,
    val budgetRepository: ItemRepository<Budget>,
    val hotelRepository: ItemRepository<Hotel>,
    val noteRepository: ItemRepository<Note>,
    val packingListRepository: ItemRepository<PackingList>,
    val routeRepository: ItemRepository<Route>,
    val ticketRepository: ItemRepository<Ticket>,
    val geocodingRepository: GeocodingRepository
) : ViewModel() {

    var tripId: Long = -1L
        private set

    private val _tripName = MutableStateFlow("")
    val tripName: StateFlow<String> = _tripName.asStateFlow()

    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate: StateFlow<LocalDate?> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate: StateFlow<LocalDate?> = _endDate.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadTrip(id: Long) {
        if (tripId == id) return // уже загружено — не дублируем при повторных вызовах LaunchedEffect
        tripId = id

        viewModelScope.launch {
            _isLoading.value = true
            repository.getTripById(id).collect { trip ->
                trip?.let {
                    _tripName.value = it.title
                    _startDate.value = it.startDate
                    _endDate.value = it.endDate
                }
                _isLoading.value = false
            }
        }
    }

    fun updateTripName(newName: String) {
        _tripName.value = newName
    }

    fun updateStartDate(newDate: LocalDate?) {
        _startDate.value = newDate
    }

    fun updateEndDate(newDate: LocalDate?) {
        _endDate.value = newDate
    }

    fun saveTrip() {
        viewModelScope.launch {
            val trip = Trip(
                tripId = tripId,
                title = _tripName.value.ifEmpty { "Новая поездка" },
                startDate = _startDate.value,
                endDate = _endDate.value
            )
            repository.updateTrip(trip)
        }
    }
}

class PreviewTripDetailsViewModel : TripDetailsViewModel(
    repository = object : TripRepository {
        override val allTrips = flowOf(
            listOf(
                Trip(tripId = 1, title = "Рим", startDate = null, endDate = null),
                Trip(tripId = 2, title = "Париж", startDate = null, endDate = null),
                Trip(tripId = 3, title = "Лондон", startDate = null, endDate = null)
            )
        )
        override val curTrips: Flow<List<Trip>>
            get() = TODO("Not yet implemented")
        override val pastTrips: Flow<List<Trip>>
            get() = TODO("Not yet implemented")

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

        override suspend fun countAllTrips(): Int {
            return 4
        }
    },
    geocodingRepository = object : GeocodingRepository {
        override suspend fun search(query: String): List<GeocodingResult> = emptyList()
    },
    budgetRepository = object : ItemRepository<Budget> {
        override fun getItemsByTripId(tripId: Long): Flow<List<Budget>> = flowOf(emptyList())

        override suspend fun insertItem(item: Budget): Long = 0L

        override suspend fun updateItem(item: Budget) { }

        override suspend fun deleteItem(item: Budget) { }

        override suspend fun deleteAllItems() { }
    },
    hotelRepository = object : ItemRepository<Hotel> {
        override fun getItemsByTripId(tripId: Long): Flow<List<Hotel>> = flowOf(emptyList())

        override suspend fun insertItem(item: Hotel): Long = 0L

        override suspend fun updateItem(item: Hotel) { }

        override suspend fun deleteItem(item: Hotel) { }

        override suspend fun deleteAllItems() { }
    },
    noteRepository = object : ItemRepository<Note> {
        override fun getItemsByTripId(tripId: Long): Flow<List<Note>> = flowOf(emptyList())

        override suspend fun insertItem(item: Note): Long = 0L

        override suspend fun updateItem(item: Note) { }

        override suspend fun deleteItem(item: Note) { }

        override suspend fun deleteAllItems() { }
    },
    packingListRepository = object : ItemRepository<PackingList> {
        override fun getItemsByTripId(tripId: Long): Flow<List<PackingList>> = flowOf(emptyList())

        override suspend fun insertItem(item: PackingList): Long = 0L

        override suspend fun updateItem(item: PackingList) { }

        override suspend fun deleteItem(item: PackingList) { }

        override suspend fun deleteAllItems() { }
    },
    routeRepository = object : ItemRepository<Route> {
        override fun getItemsByTripId(tripId: Long): Flow<List<Route>> = flowOf(emptyList())

        override suspend fun insertItem(item: Route): Long = 0L

        override suspend fun updateItem(item: Route) { }

        override suspend fun deleteItem(item: Route) { }

        override suspend fun deleteAllItems() { }
    },
    ticketRepository = object : ItemRepository<Ticket> {
        override fun getItemsByTripId(tripId: Long): Flow<List<Ticket>> = flowOf(emptyList())

        override suspend fun insertItem(item: Ticket): Long = 0L

        override suspend fun updateItem(item: Ticket) { }

        override suspend fun deleteItem(item: Ticket) { }

        override suspend fun deleteAllItems() { }
    }

)
