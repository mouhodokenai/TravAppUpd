package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.data.model.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.distinct


@HiltViewModel
open class PackingListViewModel @Inject constructor(
    packingListRepository: ItemRepository<PackingList>
) : ItemViewModel<PackingList>(packingListRepository) {

    val baggageItems: StateFlow<List<PackingList>> = items
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val baggageCount: StateFlow<Int> = baggageItems
        .map{ list -> list.map { it.itemId }.distinct().size }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val baggagePackedCount: StateFlow<Int> = baggageItems
        .map { list -> list.count { it.isPacked } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun undoDelete() {
        pendingDelete?.let { item ->
            addItem(item.copy())
            pendingDelete = null
        }
    }
}

class PreviewPackingListViewModel : PackingListViewModel(
    packingListRepository = object : ItemRepository<PackingList>{

        private val fakeData = listOf(
            PackingList(1, 1, "Еда"),
            PackingList(2, 1, "Одежда"),
            PackingList(3, 1, "Документы", true)
        )

        override fun getItemsByTripId(tripId: Long): Flow<List<PackingList>> = flowOf(fakeData)

        override suspend fun insertItem(item: PackingList): Long = 1L

        override suspend fun updateItem(item: PackingList) { }

        override suspend fun deleteItem(item: PackingList) { }

        override suspend fun deleteAllItems() { }

    }
)