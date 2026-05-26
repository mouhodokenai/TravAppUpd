package com.example.travappupd.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Trip
import com.example.travappupd.data.model.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

class TripViewModel(
    private val repository: TripRepository
) : ViewModel() {

    private val _selectedTripId = MutableStateFlow<Long?>(null)
    val selectedTripId: StateFlow<Long?> = _selectedTripId.asStateFlow()

    val currentTrip: Flow<Trip?> = _selectedTripId.flatMapLatest { id ->
        if (id != null) {
            repository.getTripById(id)
        } else {
            flowOf(null)
        }
    }

    private val _tripItems = MutableStateFlow<List<Trip>>(emptyList())
    val tripItems: Flow<List<Trip>> = repository.allTrips

    //private val _stats =MutableStateFlow<List<Stats>>(emptyList())
    //val stats: StateFlow<List<Stats>> = _stats

    /*
    fun getStats(context: Context): Flow<Stats> =
        combine(
            tripDao.getAllTrips2(),
            routeDao.getAllRoutes2()
        ) { trips, routes ->
            Log.d("StatsFlow", "Trips: ${trips.size}, Routes: ${routes.size}")

            val tripCount = trips.size
            val cityCount = routes.map { it.placeName }.distinct().size

            val geocoder = Geocoder(context, Locale.getDefault())
            val countryNames = routes.mapNotNull { route ->
                try {
                    val addresses = geocoder.getFromLocation(route.latitude, route.longitude, 1)
                    addresses?.firstOrNull()?.countryName
                } catch (e: Exception) {
                    null
                }
            }.toSet()

            Log.d("StatsFlow", "Country count: ${countryNames.size}")

            Stats(
                tripCount = tripCount,
                cityCount = cityCount,
                countryCount = countryNames.size
            )
        }


    fun dateToMillis(dateString: String): Long {
        return try {
            val date = dateFormat.parse(dateString)
            date?.time ?: 0L
        } catch (e: ParseException) {
            Log.e("TripsDebug", "Ошибка парсинга даты: $dateString", e)
            0L
        }
    }

    val todayStartMillis: Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val upcomingTrips: StateFlow<List<TripWithRoutes>> = tripDao.getAllTrips2()
        .map { trips ->
            Log.d("TripsDebug", "Всего поездок из БД: ${trips.size}")

            val filtered = trips.filter { trip ->
                val tripMillis = dateToMillis(trip.end_date)
                Log.d("TripsDebug", "Поездка: ${trip.title}, end_date: ${trip.end_date}, millis: $tripMillis, todayStartMillis: $todayStartMillis")
                tripMillis >= todayStartMillis
            }

            Log.d("TripsDebug", "Подходящих поездок: ${filtered.size}")
            filtered
        }
        .map { trips ->
            trips.map { trip ->
                val locations = routeDao.getRoutesForTrip(trip.tripId)
                    .take(3)
                    .map { it.placeName }
                Log.d("TripsDebug", "Поездка: ${trip.title}, места: $locations")
                TripWithRoutes(trip, locations)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val comingTrips: StateFlow<List<TripWithRoutes>> = tripDao.getAllTrips2()
        .map { trips ->
            trips.filter { trip ->
                dateToMillis(trip.end_date) < todayStartMillis
            }
        }
        .map { trips ->
            trips.map { trip ->
                val locations = routeDao.getRoutesForTrip(trip.tripId)
                    .take(3)
                    .map { it.placeName }
                TripWithRoutes(trip, locations)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun onTripDoubleClick(tripId: Long, title: String, onNavigateToEdit: (Long, String) -> Unit) {
        onNavigateToEdit(tripId, title)
    }
*/

    fun addTrip(title: String, start: String, end: String) {
        viewModelScope.launch {
            val trip = Trip(title = title, startDate = start, endDate = end)
            repository.insertTrip(trip)
        }
    }

    fun getTrip(tripId: Long) {
        viewModelScope.launch {
            _selectedTripId.value = tripId
        }
    }

    fun updateTrip(trip: Trip) {
        viewModelScope.launch {
            repository.updateTrip(trip)
        }
    }

    fun deleteTrip(item: Trip) {
        viewModelScope.launch {
            repository.deleteTrip(item)
        }
    }


}