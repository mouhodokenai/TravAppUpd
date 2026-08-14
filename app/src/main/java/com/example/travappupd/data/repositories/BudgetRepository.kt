package com.example.travappupd.data.repositories

import com.example.travappupd.data.dao.BudgetDao
import com.example.travappupd.data.entities.Budget
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) : ItemRepository<Budget> {

    override fun getItemsByTripId(tripId: Long): Flow<List<Budget>> {
        return  budgetDao.getByTripId(tripId)
    }

    override suspend fun insertItem(item: Budget): Long {
        return budgetDao.insert(item)
    }

    override suspend fun updateItem(item: Budget) {
        return budgetDao.update(item)
    }

    override suspend fun deleteItem(item: Budget) {
        return budgetDao.delete(item)
    }

    override suspend fun deleteAllItems() {
        return budgetDao.deleteAllBudgets()
    }
}

