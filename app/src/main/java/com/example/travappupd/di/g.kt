package com.example.travappupd.di

import android.content.Context
import androidx.room.Room
import com.example.travappupd.data.dao.BudgetDao
import com.example.travappupd.data.dao.HotelDao
import com.example.travappupd.data.dao.NoteDao
import com.example.travappupd.data.dao.PackingListDao
import com.example.travappupd.data.dao.RouteDao
import com.example.travappupd.data.dao.TicketDao
import com.example.travappupd.data.dao.TripDao
import com.example.travappupd.data.database.TravDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TravDatabase {
        return Room.databaseBuilder(
            context,
            TravDatabase::class.java,
            "trav_database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTripDao(database: TravDatabase): TripDao {
        return database.tripDao()
    }

    @Provides
    @Singleton
    fun provideBudgetDao(database: TravDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    @Singleton
    fun provideHotelDao(database: TravDatabase): HotelDao {
        return database.hotelDao()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: TravDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun providePackingListDao(database: TravDatabase): PackingListDao {
        return database.packingListDao()
    }

    @Provides
    @Singleton
    fun provideRouteDao(database: TravDatabase): RouteDao {
        return database.routeDao()
    }

    @Provides
    @Singleton
    fun provideTicketDao(database: TravDatabase): TicketDao {
        return database.ticketDao()
    }

}