package com.example.travappupd.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate


@Entity(tableName = "trip")
@TypeConverters(Converter::class)
data class Trip(
    @PrimaryKey @ColumnInfo(name = "trip_id") val tripId: Long = generateLocalId(),
    val title: String,
    @ColumnInfo(name = "start_date") val startDate: LocalDate?,
    @ColumnInfo(name = "end_date") val endDate: LocalDate?
)

class Converter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let {
            LocalDate.parse(it)
        }
    }
}