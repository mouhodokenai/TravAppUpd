package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.model.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TripViewModel() : ViewModel() {
    private val _tripName = MutableStateFlow(" ")
    val tripName: StateFlow<String> = _tripName.asStateFlow()

    private val _startDate = MutableStateFlow("выбрать")
    val startDate: StateFlow<String> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow("выбрать")
    val endDate: StateFlow<String> = _endDate.asStateFlow()


    fun updateTripName(newName: String) {
        _tripName.value = newName

    }

    fun updateStartDate(newDate: String) {
        _startDate.value = newDate
    }

    fun updateEndDate(newDate: String) {
        _endDate.value = newDate
    }

    fun saveTrip() {
        viewModelScope.launch {
            println("Сохранена поездка: ${_tripName.value}")
        }
    }


}