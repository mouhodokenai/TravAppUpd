package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.repositories.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
open class BudgetViewModel @Inject constructor(
    budgetRepository: ItemRepository<Budget>
) : ItemViewModel<Budget>(budgetRepository) {

    val budgetItems: StateFlow<List<Budget>> = items
        .map { list -> list.sortedByDescending { it.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalsByCurrency: StateFlow<List<CurrencyTotal>> = budgetItems
        .map { list ->
            list.groupBy { it.currency }
                .map { (currency, items) -> CurrencyTotal(currency, items.sumOf { it.amount }) }
                .sortedByDescending { it.amount }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoryCount: StateFlow<Int> = budgetItems
        .map { list -> list.map { it.category }.distinct().size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun undoDelete() {
        pendingDelete?.let { item ->
            addItem(item.copy())
            pendingDelete = null
        }
    }
}

data class CurrencyTotal(val currency: String, val amount: Double)

class PreviewBudgetViewModel : BudgetViewModel(
    budgetRepository = object : ItemRepository<Budget> {
        private val fakeData = listOf(

            Budget(budgetId = 1, tripId = 1, category = "flight", amount = 45000.0, currency = "₽"),
            Budget(budgetId = 2, tripId = 1, category = "hotel", amount = 62000.0, currency = "₽"),
            Budget(budgetId = 3, tripId = 1, category = "food", amount = 18500.0, currency = "₽"),
            Budget(budgetId = 4, tripId = 1, category = "transport", amount = 12000.0, currency = "₽"),
            Budget(budgetId = 5, tripId = 1, category = "Подарки", amount = 5000.0, currency = "$")
        )

        override fun getItemsByTripId(tripId: Long): Flow<List<Budget>> = flowOf(emptyList())
            //flowOf(fakeData)

        override suspend fun insertItem(item: Budget): Long = 0L
        override suspend fun updateItem(item: Budget) = Unit
        override suspend fun deleteItem(item: Budget) = Unit
        override suspend fun deleteAllItems() {

        }
    }
) {
    init {
        selectTrip(1L)
    }
}