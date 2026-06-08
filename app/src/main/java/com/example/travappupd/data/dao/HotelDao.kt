package com.example.travappupd.data.dao

import androidx.room.*
import com.example.travappupd.data.entities.Hotel
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelDao {
    @Insert
    suspend fun insert(hotel: Hotel) : Long

    @Update
    suspend fun update(hotel: Hotel)

    @Delete
    suspend fun delete(hotel: Hotel)

    @Query("SELECT * FROM hotel WHERE trip_id = :tripId")
    fun getHotelsForTrip(tripId: Long): Flow<List<Hotel>>

    @Query("DELETE FROM hotel")
    suspend fun deleteAllHotels()
}

