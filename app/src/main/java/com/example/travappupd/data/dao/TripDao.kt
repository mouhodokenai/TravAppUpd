package com.example.travappupd.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.travappupd.data.entities.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: Trip): Long

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)


    @Query("SELECT * FROM trip")
    fun getAllTrips(): Flow<List<Trip>>

    @Query("SELECT * FROM trip WHERE trip_id = :tripId")
    fun getByTripId(tripId: Long): Flow<Trip?>

    @Query("DELETE FROM trip WHERE trip_id = :tripId")
    suspend fun deleteTripById(tripId: Long)

    @Query("DELETE FROM trip")
    suspend fun deleteAllTrips()


}


