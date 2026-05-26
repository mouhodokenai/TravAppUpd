package com.example.travappupd.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.model.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class ItemViewModel<Item>(private val repository: ItemRepository<Item>) : ViewModel() {
    private val _selectedTripId = MutableStateFlow<Long?>(null)
    val selectedTripId: StateFlow<Long?> = _selectedTripId.asStateFlow()
    val items: Flow<List<Item>> = _selectedTripId.flatMapLatest { tripId ->
        if (tripId != null) {
            repository.getItemsByTripId(tripId)
        } else {
            flowOf(emptyList())
        }
    }

    private val _items = MutableStateFlow<List<Item>>(emptyList())

    fun selectTrip(tripId: Long) {
        _selectedTripId.value = tripId
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }

    fun updateBudget(item: Item) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun deleteBudget(item: Item) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
}