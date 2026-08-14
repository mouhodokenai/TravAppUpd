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
import com.example.travappupd.data.entities.Hotel
import com.example.travappupd.data.entities.Note
import com.example.travappupd.data.entities.PackingList
import com.example.travappupd.data.entities.Route
import com.example.travappupd.data.entities.Ticket
import com.example.travappupd.data.repositories.BudgetRepository
import com.example.travappupd.data.repositories.ItemRepository
import com.example.travappupd.data.model.repository.NoteRepository
import com.example.travappupd.data.model.repository.PackingListRepository
import com.example.travappupd.data.model.repository.TicketRepository
import com.example.travappupd.data.remote.NominatimApi
import com.example.travappupd.data.repositories.TripRepository
import com.example.travappupd.data.repositories.DraftTripRepository
import com.example.travappupd.data.repositories.GeocodingRepository
import com.example.travappupd.data.repositories.GeocodingRepositoryImpl
import com.example.travappupd.data.repositories.HotelRepository
import com.example.travappupd.data.repositories.RouteRepository
import com.example.travappupd.data.repositories.TripRepositoryImpl
import dagger.Binds
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
    fun provideHotelRepository(
        hotelDao: HotelDao
    ): ItemRepository<Hotel> {
        return HotelRepository(hotelDao)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        noteDao: NoteDao
    ): ItemRepository<Note> {
        return NoteRepository(noteDao)
    }

    @Provides
    @Singleton
    fun providePackingListRepository(
        packingListDao: PackingListDao
    ): ItemRepository<PackingList> {
        return PackingListRepository(packingListDao)
    }

    @Provides
    @Singleton
    fun provideTicketRepository(
        ticketDao: TicketDao
    ): ItemRepository<Ticket> {
        return TicketRepository(ticketDao)
    }

    @Provides
    @Singleton
    fun provideRouteRepository(
        routeDao: RouteDao
    ): ItemRepository<Route> {
        return RouteRepository(routeDao)
    }

    @Provides
    @Singleton
    fun provideGeocodingRepository(
        api: NominatimApi
    ): GeocodingRepository {
        return GeocodingRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDraftTripRepository() = DraftTripRepository()
}