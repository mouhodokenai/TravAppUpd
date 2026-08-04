package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Ticket
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
import javax.inject.Inject

@HiltViewModel
open class TicketViewModel @Inject constructor(
    private val ticketRepository: ItemRepository<Ticket>
) : ItemViewModel<Ticket>(ticketRepository) {

    val ticketItems: StateFlow<List<Ticket>> = items
        .map { list -> list.sortedBy { it.departureDate } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun undoDelete() {
        pendingDelete?.let { ticket ->
            addItem(ticket.copy())
            pendingDelete = null
        }
    }

}

class PreviewTicketViewModel : TicketViewModel(
    ticketRepository = object : ItemRepository<Ticket> {
        private val fakeData = listOf(
            Ticket(
                ticketId = 1,
                tripId = 1,
                transportType = "flight",
                departureCity = "Москва",
                arrivalCity = "Рим",
                departureTime = LocalTime.parse("14:30"),
                arrivalTime = LocalTime.parse("17:45"),
                departureDate = LocalDate.parse("2026-08-01"),
                arrivalDate = LocalDate.parse("2026-08-01"),
                ticketNumber = "SU-2145"
            )
        )

        override fun getItemsByTripId(tripId: Long): Flow<List<Ticket>> = flowOf(emptyList())
        override suspend fun insertItem(item: Ticket): Long = 0L
        override suspend fun updateItem(item: Ticket) = Unit
        override suspend fun deleteItem(item: Ticket) = Unit
        override suspend fun deleteAllItems() {}
    }
) {
    init { selectTrip(1L) }
}