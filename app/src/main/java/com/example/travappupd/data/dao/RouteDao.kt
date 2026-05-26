package com.example.travappupd.data.dao

import androidx.room.*
import com.example.travappupd.data.entities.Route
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {

    @Insert
    suspend fun insert(route: Route) : Long

    @Update
    suspend fun update(route: Route): Unit

    @Delete
    suspend fun delete(route: Route): Unit

    @Query("SELECT * FROM route")
    fun getAllRoutes(): Flow<List<Route>>

    @Query("SELECT * FROM route WHERE trip_id = :tripId ORDER BY order_index ASC")
    fun getByTripId(tripId: Long): Flow<List<Route>>

    @Query("DELETE FROM route WHERE trip_id = :tripId")
    suspend fun clearAllRoutesForTrip(tripId: Long): Unit

    /*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routes: List<Route>)
     */

    @Query("DELETE FROM route")
    suspend fun deleteAllRoutes(): Unit
}


