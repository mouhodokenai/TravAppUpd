package com.example.travappupd.data.model.repository

import com.example.travappupd.data.dao.NoteDao
import com.example.travappupd.data.entities.Note
import com.example.travappupd.data.repositories.ItemRepository
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) : ItemRepository<Note> {

    override fun getItemsByTripId(tripId: Long): Flow<List<Note>> {
        return noteDao.getByTripId(tripId)
    }

    override suspend fun insertItem(item: Note): Long {
        return noteDao.insert(item)
    }

    override suspend fun updateItem(item: Note) {
        return noteDao.update(item)
    }

    override suspend fun deleteItem(item: Note) {
        return noteDao.delete(item)
    }

    override suspend fun deleteAllItems() {
        return noteDao.deleteAllNotes()
    }
}