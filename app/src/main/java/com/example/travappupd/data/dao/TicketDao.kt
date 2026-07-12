package com.example.travappupd.data.dao

import androidx.room.*
import com.example.travappupd.data.entities.Ticket
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ticket: Ticket): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tickets: List<Ticket>)

    @Update
    suspend fun update(ticket: Ticket)

    @Delete
    suspend fun delete(ticket: Ticket)

    @Query("SELECT * FROM tickets WHERE trip_id = :tripId")
    fun getByTripId(tripId: Long): Flow<List<Ticket>>

    @Query("DELETE FROM tickets")
    suspend fun deleteAllTickets()
}




