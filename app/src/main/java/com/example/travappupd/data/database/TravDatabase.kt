package com.example.travappupd.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.dao.BudgetDao
import com.example.travappupd.data.dao.HotelDao
import com.example.travappupd.data.dao.NoteDao
import com.example.travappupd.data.dao.PackingListDao
import com.example.travappupd.data.dao.RouteDao
import com.example.travappupd.data.dao.TicketDao
import com.example.travappupd.data.dao.TripDao
import com.example.travappupd.data.entities.Hotel
import com.example.travappupd.data.entities.Note
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.data.entities.Trip


@Database(
    entities = [
        Budget::class,
        Hotel::class,
        Note::class,
        PackingList::class,
        Route::class,
        Ticket::class,
        Trip::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TravDatabase : RoomDatabase() {

    abstract fun budgetDao(): BudgetDao
    abstract fun hotelDao(): HotelDao
    abstract fun noteDao(): NoteDao
    abstract fun packingListDao(): PackingListDao
    abstract fun routeDao(): RouteDao
    abstract fun ticketDao(): TicketDao
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile
        private var INSTANCE: TravDatabase? = null

        fun getInstance(context: Context): TravDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravDatabase::class.java,
                    "trav_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
