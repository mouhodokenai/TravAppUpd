package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.repositories.GeocodingRepository
import com.example.travappupd.data.repositories.GeocodingResult
import com.example.travappupd.data.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class RouteViewModel @Inject constructor(
    routeRepository: ItemRepository<Route>,
    private val geocodingRepository: GeocodingRepository
) : ItemViewModel<Route>(routeRepository) {

    val routeItems: StateFlow<List<Route>> = items
        .map { list -> list.sortedBy { it.orderIndex } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<GeocodingResult>>(emptyList())
    val searchResults: StateFlow<List<GeocodingResult>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private var searchJob: Job? = null

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()

        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // debounce — не долбим Nominatim на каждую букву
            _isSearching.value = true
            _searchResults.value = geocodingRepository.search(query)
            _isSearching.value = false
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }


    fun undoDelete() {
        pendingDelete?.let { route ->
            addItem(route.copy())
            pendingDelete = null
        }
    }

    fun addRoutePoint(name: String, address: String?, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val tripId = selectedTripId.value ?: return@launch
            val nextIndex = (routeItems.value.maxOfOrNull { it.orderIndex } ?: -1) + 1
            val route = Route(
                tripId = tripId,
                name = name,
                address = address,
                latitude = latitude,
                longitude = longitude,
                orderIndex = nextIndex
            )
            addItem(route)
        }
    }

    fun reorderItems(newOrder: List<Route>) {
        viewModelScope.launch {
            newOrder.forEachIndexed { index, route ->
                if (route.orderIndex != index) {
                    updateItem(route.copy(orderIndex = index))
                }
            }
        }
    }
}

class PreviewRouteViewModel : RouteViewModel(
    routeRepository = object : ItemRepository<Route> {
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

        override fun getItemsByTripId(tripId: Long): Flow<List<Route>> = flowOf(emptyList())
        override suspend fun insertItem(item: Route): Long = 0L
        override suspend fun updateItem(item: Route) = Unit
        override suspend fun deleteItem(item: Route) = Unit
        override suspend fun deleteAllItems() {}
    },

    geocodingRepository = object : GeocodingRepository {
        override suspend fun search(query: String): List<GeocodingResult> {
            return if (query.isBlank()) {
                emptyList()
            } else {
                listOf(
                    GeocodingResult(
                        name = "Тестовое место",
                        address = "Тестовый адрес, $query",
                        latitude = 41.9,
                        longitude = 12.5
                    )
                )
            }
        }
    }
) {
    init { selectTrip(1L) }
}