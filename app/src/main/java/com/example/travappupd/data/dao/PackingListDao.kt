package com.example.travappupd.data.dao

import androidx.room.*
import com.example.travappupd.data.entities.PackingList
import kotlinx.coroutines.flow.Flow

@Dao
interface PackingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PackingList) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PackingList>)

    @Update
    suspend fun update(item: PackingList)

    @Delete
    suspend fun delete(item: PackingList)

    @Query("SELECT * FROM packing_list")
    fun getAll(): Flow<List<PackingList>>

    @Query("SELECT * FROM packing_list WHERE trip_id = :tripId")
    fun getByTripId(tripId: Long): Flow<List<PackingList>>

    @Query("DELETE FROM trip")
    suspend fun deleteAllPackingItems()
}


