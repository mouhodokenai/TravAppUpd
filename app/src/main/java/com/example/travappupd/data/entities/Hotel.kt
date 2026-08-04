package com.example.travappupd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalTime


@Entity(tableName = "hotel",
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
@TypeConverters(Converter::class)
data class Hotel(
    @PrimaryKey
    @ColumnInfo(name = "hotel_id") val hotelId: Long = generateLocalId(),
    @ColumnInfo(name = "trip_id") val tripId: Long,
    val name: String,
    val address: String,
    @ColumnInfo(name = "check_in_date") val checkInDate: LocalDate?,
    @ColumnInfo(name = "check_out_date") val checkOutDate: LocalDate?,
    @ColumnInfo(name = "check_in_time") val checkInTime: LocalTime?,
    @ColumnInfo(name = "check_out_time") val checkOutTime: LocalTime?
)

class HotelDraft(
    val name: String,
    val address: String,
    val checkInDate: String,
    val checkOutDate: String,
    val checkInTime: String,
    val checkOutTime: String
)

