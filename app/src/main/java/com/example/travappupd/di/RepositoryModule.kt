package com.example.travappupd.di;

import com.example.travappupd.data.dao.BudgetDao
import com.example.travappupd.data.dao.HotelDao
import com.example.travappupd.data.dao.NoteDao
import com.example.travappupd.data.dao.PackingListDao
import com.example.travappupd.data.dao.RouteDao
import com.example.travappupd.data.dao.TicketDao
import com.example.travappupd.data.dao.TripDao
import com.example.travappupd.data.database.TravDatabase
import com.example.travappupd.data.entities.Budget
import com.example.travappupd.data.model.repository.BudgetRepository
import com.example.travappupd.data.model.repository.ItemRepository
import com.example.travappupd.data.repositories.TripRepository
import com.example.travappupd.data.repositories.DraftTripRepository
import com.example.travappupd.data.repositories.TripRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTripRepository(
        db: TravDatabase,
        budgetDao: BudgetDao,
        hotelDao: HotelDao,
        noteDao: NoteDao,
        packingListDao: PackingListDao,
        routeDao: RouteDao,
        ticketDao: TicketDao,
        tripDao: TripDao
    ): TripRepository {
        return TripRepositoryImpl(
            db = db,
            tripDao = tripDao,
            budgetDao = budgetDao,
            hotelDao = hotelDao,
            noteDao = noteDao,
            packingListDao = packingListDao,
            routeDao = routeDao,
            ticketDao = ticketDao
        )
    }

    @Provides
    @Singleton
    fun provideBudgetRepository(
        budgetDao: BudgetDao
    ): ItemRepository<Budget> {
        return BudgetRepository(budgetDao)
    }

    @Provides
    @Singleton
    fun provideDraftTripRepository() = DraftTripRepository()
}