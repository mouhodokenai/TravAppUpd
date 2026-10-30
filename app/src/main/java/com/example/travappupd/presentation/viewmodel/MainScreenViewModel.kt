package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.dao.RouteDao
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.entities.Hotel
import com.example.travappupd.data.entities.Note
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.data.entities.Trip
import com.example.travappupd.data.repositories.GeocodingRepository
import com.example.travappupd.data.repositories.ItemRepository
import com.example.travappupd.data.repositories.RouteRepository
import com.example.travappupd.data.repositories.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainScreenViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val routeRepository: RouteRepository
) : ViewModel()
{

    private val _totalTripsCount = MutableStateFlow(0)
    val totalTripsCount: StateFlow<Int> = _totalTripsCount.asStateFlow()

    private val _totalPlacesCount = MutableStateFlow(0)
    val totalPlacesCount: StateFlow<Int> = _totalPlacesCount.asStateFlow()

    private val _totalCountriesCount = MutableStateFlow(0)
    val totalCountriesCount: StateFlow<Int> = _totalCountriesCount.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _totalTripsCount.value = tripRepository.countAllTrips()
            _totalPlacesCount.value = routeRepository.countAllItems()
            _totalCountriesCount.value = routeRepository.countDistinctCountries()
        }
    }


}

class PreviewMainScreenViewModel : MainScreenViewModel(
    tripRepository = object : TripRepository {
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

        override suspend fun countAllTrips(): Int = 2
    },
    routeRepository = RouteRepository(
        routeDao = object : RouteDao {
            private val fakeData = listOf(
                Route(
                    routeId = 1, tripId = 1, name = "Колизей",
                    address = "Piazza del Colosseo, 1, Roma",
                    latitude = 41.8902, longitude = 12.4922,
                    orderIndex = 0
                ),
                Route(
                    routeId = 2, tripId = 1, name = "Пантеон",
                    address = "Piazza della Rotonda, Roma",
                    latitude = 41.8986, longitude = 12.4769,
                    orderIndex = 1
                ),
                Route(
                    routeId = 3, tripId = 1, name = "Фонтан Треви",
                    address = "Piazza di Trevi, Roma",
                    latitude = 41.9009, longitude = 12.4833,
                    orderIndex = 2
                )
            )

            override suspend fun insertAll(routes: List<Route>) { }

            override suspend fun insert(route: Route): Long = 0L

            override suspend fun update(route: Route) { }

            override suspend fun updateAll(routes: List<Route>) { }

            override suspend fun delete(route: Route) { }

            override fun getByTripId(tripId: Long): Flow<List<Route>> = flowOf(fakeData)

            override fun getAll(): Flow<List<Route>> = flowOf(fakeData)

            override suspend fun countAll(): Int = fakeData.size

            override suspend fun getMaxOrderIndex(tripId: Long): Int = 1

            override suspend fun deleteAll() { }
            override suspend fun countDistinctCountries(): Int = 2
        }
    )
)