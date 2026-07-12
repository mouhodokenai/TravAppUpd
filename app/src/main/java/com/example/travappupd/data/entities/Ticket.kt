package com.example.travappupd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tickets",
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
data class Ticket(
    @PrimaryKey
    @ColumnInfo(name = "ticket_id") val ticketId: Long = generateLocalId(),
    @ColumnInfo(name = "trip_id") val tripId: Long,
    @ColumnInfo(name = "transport_type") val transportType: String,
    @ColumnInfo(name = "departure_city") val departureCity: String,
    @ColumnInfo(name = "arrival_city") val arrivalCity: String,
    @ColumnInfo(name = "departure_time") val departureTime: String,
    @ColumnInfo(name = "arrival_time") val arrivalTime: String,
    @ColumnInfo(name = "departure_date") val departureDate: String,
    @ColumnInfo(name = "arrival_date") val arrivalDate: String,
    @ColumnInfo(name = "ticket_number") val ticketNumber: String
)

data class TicketDraft(
    val transportType: String,
    val departureCity: String,
    val arrivalCity: String,
    val departureTime: String,
    val arrivalTime: String,
    val departureDate: String,
    val arrivalDate: String,
    val ticketNumber: String
)