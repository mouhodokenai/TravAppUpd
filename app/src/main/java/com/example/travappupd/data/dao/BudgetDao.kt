package com.example.travappupd.data.dao

import androidx.room.*
import com.example.travappupd.data.entities.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert
    suspend fun insert(budget: Budget) : Long

    @Update
    suspend fun update(budget: Budget): Unit

    @Delete
    suspend fun delete(budget: Budget): Unit

    @Query("SELECT * FROM budget WHERE trip_id = :tripId")
    fun getByTripId(tripId: Long): Flow<List<Budget>>

    @Query("DELETE FROM budget")
    suspend fun deleteAllBudgets(): Unit
}






