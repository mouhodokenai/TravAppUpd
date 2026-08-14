package com.example.travappupd.data.model.repository

import com.example.travappupd.data.dao.TicketDao
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.data.repositories.ItemRepository
import kotlinx.coroutines.flow.Flow

class TicketRepository(private val ticketDao: TicketDao) : ItemRepository<Ticket> {

    override fun getItemsByTripId(tripId: Long): Flow<List<Ticket>> {
        return ticketDao.getByTripId(tripId)
    }

    override suspend fun insertItem(item: Ticket): Long {
        return ticketDao.insert(item)
    }

    override suspend fun updateItem(item: Ticket) {
        return ticketDao.update(item)
    }

    override suspend fun deleteItem(item: Ticket) {
        return ticketDao.delete(item)
    }

    override suspend fun deleteAllItems() {
        return ticketDao.deleteAllTickets()
    }
}