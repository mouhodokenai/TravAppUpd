package com.example.travappupd.data.dao

import androidx.room.*
import com.example.travappupd.data.entities.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note) : Long

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes WHERE trip_id = :tripId")
    fun getByTripId(tripId: Long): Flow<List<Note>>

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}


