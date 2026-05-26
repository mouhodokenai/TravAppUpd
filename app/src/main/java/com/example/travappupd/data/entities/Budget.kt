package com.example.travappupd.data.entities

import androidx.room.*

@Entity(tableName = "budget",
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

data class Budget(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id") val budgetId: Int = 0,
    @ColumnInfo(name = "trip_id") val tripId: Long,
    val category: String,
    val amount: Double,
    val currency: String
)

