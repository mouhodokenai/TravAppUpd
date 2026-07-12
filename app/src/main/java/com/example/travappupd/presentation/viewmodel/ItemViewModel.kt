package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.model.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class ItemViewModel<Item>(
    protected val repository: ItemRepository<Item>
) : ViewModel() {
    private val _selectedTripId = MutableStateFlow<Long?>(0)
    val selectedTripId: StateFlow<Long?> = _selectedTripId.asStateFlow()

    val items: Flow<List<Item>> = _selectedTripId.flatMapLatest { tripId ->
        if (tripId != null) {
            repository.getItemsByTripId(tripId)
        } else {
            flowOf(emptyList())
        }
    }

    fun selectTrip(tripId: Long) {
        _selectedTripId.value = tripId
    }

    fun addItem(item: Item) {
        viewModelScope.launch { repository.insertItem(item) }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch { repository.updateItem(item) }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch { repository.deleteItem(item) }
    }
}
