package com.example.travappupd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "route",
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
data class Route(
    @PrimaryKey
    @ColumnInfo(name = "route_id") val routeId: Long = generateLocalId(),
    @ColumnInfo(name = "trip_id") val tripId: Long,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    @ColumnInfo(name = "order_index") val orderIndex: Int? = null
)

data class RouteDraft(
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val orderIndex: Int? = null
)
