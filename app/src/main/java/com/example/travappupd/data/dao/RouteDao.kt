package com.example.travappupd.data.dao

import androidx.room.*
import com.example.travappupd.data.entities.Route
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routes: List<Route>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(route: Route): Long

    @Update
    suspend fun update(route: Route)

    @Update
    suspend fun updateAll(routes: List<Route>)

    @Delete
    suspend fun delete(route: Route)

    @Query("SELECT * FROM route WHERE trip_id = :tripId ORDER BY order_index ASC")
    fun getByTripId(tripId: Long): Flow<List<Route>>

    @Query("SELECT COALESCE(MAX(order_index), -1) FROM route WHERE trip_id = :tripId")
    suspend fun getMaxOrderIndex(tripId: Long): Int

    @Query("DELETE FROM route")
    suspend fun deleteAll()
}


