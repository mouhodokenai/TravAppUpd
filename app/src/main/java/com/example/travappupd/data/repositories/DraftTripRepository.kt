package com.example.travappupd.data.repositories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.entities.BudgetDraft
import com.example.travappupd.data.entities.Hotel
import com.example.travappupd.data.entities.HotelDraft
import com.example.travappupd.data.entities.Note
import com.example.travappupd.data.entities.NoteDraft
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.data.entities.PackingListDraft
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.entities.RouteDraft
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.data.entities.TicketDraft
import com.example.travappupd.data.entities.Trip
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DraftTripRepository @Inject constructor() {

    var title by mutableStateOf("")

    var startDate by mutableStateOf<LocalDate?>(null)

    var endDate by mutableStateOf<LocalDate?>(null)

    val budgets = mutableStateListOf<BudgetDraft>()
    val baggage = mutableStateListOf<PackingListDraft>()
    val hotels = mutableStateListOf<HotelDraft>()
    val notes = mutableStateListOf<NoteDraft>()
    val routes = mutableStateListOf<RouteDraft>()
    val tickets = mutableStateListOf<TicketDraft>()

    fun clear() {
        title = ""
        startDate = null
        endDate = null

        budgets.clear()
        baggage.clear()
        hotels.clear()
        notes.clear()
        routes.clear()
        tickets.clear()
    }
}