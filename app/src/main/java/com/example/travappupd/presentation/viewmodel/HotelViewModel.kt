package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.entities.Hotel
import com.example.travappupd.data.model.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

@HiltViewModel
open class HotelViewModel @Inject constructor(
    hotelRepository: ItemRepository<Hotel>
) : ItemViewModel<Hotel>(hotelRepository) {

    val hotelItems: StateFlow<List<Hotel>> = items
        .map { list -> list.sortedBy { it.checkInDate } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalNights: StateFlow<Int> = hotelItems
        .map { list -> list.sumOf { nightsBetween(it.checkInDate!!, it.checkOutDate!!) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)


    fun undoDelete() {
        pendingDelete?.let { item ->
            addItem(item.copy())
            pendingDelete = null
        }
    }
}

fun nightsBetween(checkIn: LocalDate, checkOut: LocalDate): Int {
    return try {
        ChronoUnit.DAYS.between(checkIn, checkOut).toInt()
    } catch (     e: Exception) {
        -1
    }
}

class PreviewHotelViewModel : HotelViewModel(
    hotelRepository = object : ItemRepository<Hotel> {
        private val fakeData = listOf(
            Hotel(
                hotelId = 1,
                tripId = 1,
                name = "Grand Plaza",
                address = "Via Roma, 12",
                checkInDate = LocalDate.of(2026, 8, 1),
                checkOutDate = LocalDate.of(2026, 8, 5),
                checkInTime = LocalTime.of(14, 0),
                checkOutTime = LocalTime.of(11, 0)
            )
        )

        override fun getItemsByTripId(tripId: Long): Flow<List<Hotel>> = flowOf(fakeData)
        override suspend fun insertItem(item: Hotel): Long = 0L
        override suspend fun updateItem(item: Hotel) = Unit
        override suspend fun deleteItem(item: Hotel) = Unit
        override suspend fun deleteAllItems() {}
    }
) {
    init {
        selectTrip(1L)
    }
}