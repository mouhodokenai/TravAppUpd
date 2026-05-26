package com.example.travappupd.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.travappupd.data.entities.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert
    fun insert(trip: Trip): Long

    @Update
    fun update(trip: Trip): Unit

    @Delete
    fun delete(trip: Trip): Unit


    @Query("SELECT * FROM trip")
    fun getAllTrips(): Flow<List<Trip>>

    @Query("SELECT * FROM trip WHERE trip_id = :tripId")
    fun getByTripId(tripId: Long): Flow<Trip?>

    @Query("DELETE FROM trip")
    fun deleteAllTrips(): Unit


}


