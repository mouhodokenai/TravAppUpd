package com.example.travappupd

/*
import android.content.Context
import androidx.room.Room
import com.example.travappupd.data.entity.TripDao
import com.example.travappupd.data.model.repository.TripRepository
import com.example.travappupd.data.model.repository.TripRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModel {
    @Provides
    @Singleton
    fun provideDatabase(dao: TripDao) : TripRepository {
        return TripRepositoryImpl(dao)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TripDatabase {
        return Room.databaseBuilder(
            context,
            TripDatabase::class.java,
            "trip_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTripDao(
        database: TripDatabase
    ): TripDao {
        return database.tripDao()
    }
}
*/