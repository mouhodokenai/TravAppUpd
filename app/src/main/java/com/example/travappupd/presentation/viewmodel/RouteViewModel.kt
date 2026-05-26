package com.example.travappupd.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.model.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class RouteViewModel(private val repository: RouteRepository) : ViewModel() {

    private val _selectedTripId = MutableStateFlow<Long?>(null)
    val selectedTripId: StateFlow<Long?> = _selectedTripId.asStateFlow()
    val routeItems: Flow<List<Route>> = _selectedTripId.flatMapLatest { tripId ->
        if (tripId != null) {
            repository.getItemsByTripId(tripId)
        } else {
            flowOf(emptyList())
        }
    }

    private val _routeItems = MutableStateFlow<List<Route>>(emptyList())

    fun selectTrip(tripId: Long) {
        _selectedTripId.value = tripId
    }

    /*
    fun loadRoutes(tripId: Long) {
            val routes = routeDao.getRoutesForTrip(tripId)
            _routeItems.postValue(routes)
        }
    }
    */


    fun addRoute(route: Route) {
        viewModelScope.launch {
            repository.insertItem(route)
        }
    }

    fun updateRoute(route: Route) {
        viewModelScope.launch {
            repository.updateItem(route)
        }
    }

    fun deleteRoute(route: Route) {
        viewModelScope.launch {
            repository.deleteItem(route)
        }
    }

    /*
    fun getRoute(id: Int) {
        viewModelScope.launch {

        }
    }


    fun moveRouteUp(index: Int, route: Route) {
        viewModelScope.launch {
            val routes = routeDao.getRoutesForTrip(route.tripId).toMutableList();
            if (index > 0) {
                Collections.swap(routes, index, index - 1)
                routeDao.clearAllRoutesForTrip(route.tripId)
                routeDao.insertAll(routes)
                loadRoutes(route.tripId)
            }
        }
    }

    fun moveRoute(fromIndex: Int, toIndex: Int) {
        val currentRoutes = routeItems.value?.toMutableList() ?: return
        if (fromIndex in currentRoutes.indices && toIndex in currentRoutes.indices) {
            val item = currentRoutes.removeAt(fromIndex)
            currentRoutes.add(toIndex, item)

            viewModelScope.launch {
                currentRoutes.forEachIndexed { index, route ->
                    if (route.orderIndex != index) {
                        viewModelScope.launch {
                            routeDao.update(route.copy(orderIndex = index))
                        }
                    }
                }
                _routeItems.postValue(currentRoutes)
            }
        }
    }


    private fun reorderRoutes(tripId: Long) {
        viewModelScope.launch {
            val routes = routeDao.getRoutesForTrip(tripId)
            val reordered = routes.mapIndexed { i, route -> route.copy(orderIndex = i) }
            routeDao.clearAllRoutesForTrip(tripId)
            routeDao.insertAll(reordered)
        }
    }


    fun selectPlace(route: Route) {
        _selectedPlace.value = route
    }

    fun clearSelectedPlace() {
        _selectedPlace.value = null
    }
*/

}