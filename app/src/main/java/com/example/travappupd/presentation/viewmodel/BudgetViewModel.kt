package com.example.travappupd.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.model.repository.BudgetRepository
import com.example.travappupd.data.model.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class BudgetViewModel(private val repository: BudgetRepository) : ViewModel() {
    private val _selectedTripId = MutableStateFlow<Long?>(null)
    val selectedTripId: StateFlow<Long?> = _selectedTripId.asStateFlow()
    val budgetItems: Flow<List<Budget>> = _selectedTripId.flatMapLatest { tripId ->
        if (tripId != null) {
            repository.getItemsByTripId(tripId)
        } else {
            flowOf(emptyList())
        }
    }

    private val _budgetItems = MutableStateFlow<List<Budget>>(emptyList())

    fun selectTrip(tripId: Long) {
        _selectedTripId.value = tripId
    }

    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            repository.insertItem(budget)
        }
    }

    fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            repository.updateItem(budget)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            repository.deleteItem(budget)
        }
    }
}