package com.example.travappupd.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.travappupd.data.entities.Note
import com.example.travappupd.data.entities.generateLocalId
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
open class NoteViewModel @Inject constructor(
    noteRepository: ItemRepository<Note>
) : ItemViewModel<Note>(noteRepository) {

    val noteItems: StateFlow<List<Note>> = items
        .map { list -> list.sortedBy { it.title } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun undoDelete() {
        pendingDelete?.let { item ->
            addItem(item.copy(noteId = generateLocalId()))
            pendingDelete = null
        }
    }


}

class PreviewNoteViewModel : NoteViewModel(
    noteRepository = object : ItemRepository<Note> {
        private val fakeData = listOf (
            Note(1, 1, "Не забыть", "Вещи"),
            Note(2, 1, "Планы", "Планы"),
            Note(3, 1, "Сувениры", "Сувениры")
        )

        override fun getItemsByTripId(tripId: Long): Flow<List<Note>> = flowOf(fakeData)
        override suspend fun insertItem(item: Note): Long = 0L

        override suspend fun updateItem(item: Note) { }

        override suspend fun deleteItem(item: Note) { }

        override suspend fun deleteAllItems() { }

    }
)