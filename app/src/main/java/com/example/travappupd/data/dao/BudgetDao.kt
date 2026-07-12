package com.example.travappupd.data.dao

import androidx.room.*
import com.example.travappupd.data.entities.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(budgets: List<Budget>)

    @Update
    suspend fun update(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

    @Query("SELECT * FROM budget WHERE trip_id = :tripId")
    fun getByTripId(tripId: Long): Flow<List<Budget>>

    @Query("DELETE FROM budget")
    suspend fun deleteAllBudgets()

}






