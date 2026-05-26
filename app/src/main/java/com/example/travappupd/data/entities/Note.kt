package com.example.travappupd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["trip_id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index(value = ["trip_id"])]
)

data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id") val noteId: Int = 0,
    @ColumnInfo(name = "trip_id") val tripId: Long,
    val title: String,
    val content: String
)