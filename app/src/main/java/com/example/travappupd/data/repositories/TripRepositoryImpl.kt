package com.example.travappupd.data.repositories

import androidx.room.withTransaction
import com.example.travappupd.data.dao.BudgetDao
import com.example.travappupd.data.dao.HotelDao
import com.example.travappupd.data.dao.NoteDao
import com.example.travappupd.data.dao.PackingListDao
import com.example.travappupd.data.dao.RouteDao
import com.example.travappupd.data.dao.TicketDao
import com.example.travappupd.data.dao.TripDao
import com.example.travappupd.data.database.TravDatabase
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.entities.Hotel
import com.example.travappupd.data.entities.Note
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.data.entities.Trip
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val db: TravDatabase,
    private val tripDao: TripDao,
    private val budgetDao: BudgetDao,
    private val hotelDao: HotelDao,
    private val noteDao: NoteDao,
    private val packingListDao: PackingListDao,
    private val routeDao: RouteDao,
    private val ticketDao: TicketDao
) : TripRepository {

    override val allTrips: Flow<List<Trip>> = tripDao.getAllTrips()

    override fun getTripById(id: Long): Flow<Trip?> {
        return tripDao.getByTripId(id)
    }

    override suspend fun insertTrip(trip: Trip): Long {
        return tripDao.insert(trip)
    }

    override suspend fun updateTrip(trip: Trip) {
        tripDao.update(trip)
    }

    override suspend fun deleteTrip(trip: Trip) {
        tripDao.delete(trip)
    }

    override suspend fun deleteAllTrips() {
        tripDao.deleteAllTrips()
    }

    override suspend fun deleteTripById(id: Long) {
        tripDao.deleteTripById(id)
    }

    override suspend fun saveTripWithChildren(
        trip: Trip,
        budgets: List<Budget>,
        hotels: List<Hotel>,
        notes: List<Note>,
        packingItems: List<PackingList>,
        routes: List<Route>,
        tickets: List<Ticket>
    ) {
        db.withTransaction {
            tripDao.insert(trip)
            if (budgets.isNotEmpty()) budgetDao.insertAll(budgets)
            if (hotels.isNotEmpty()) hotelDao.insertAll(hotels)
            if (notes.isNotEmpty()) noteDao.insertAll(notes)
            if (packingItems.isNotEmpty()) packingListDao.insertAll(packingItems)
            if (routes.isNotEmpty()) routeDao.insertAll(routes)
            if (tickets.isNotEmpty()) ticketDao.insertAll(tickets)
        }
    }
}