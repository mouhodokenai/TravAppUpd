package com.example.travappupd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "packing_list",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["trip_id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["trip_id"])]
)

data class PackingList(
    @PrimaryKey
    @ColumnInfo(name = "item_id") val itemId: Long = generateLocalId(),
    @ColumnInfo(name = "trip_id") val tripId: Long,
    var name: String,
    @ColumnInfo(name = "is_packed") var isPacked: Boolean = false
)

data class PackingListDraft(
    val name: String,
    val isPacked: Boolean = false
)