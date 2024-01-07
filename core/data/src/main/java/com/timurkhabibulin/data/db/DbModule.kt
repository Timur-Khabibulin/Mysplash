package com.timurkhabibulin.data.db

import android.content.Context
import androidx.room.Room
import com.timurkhabibulin.domain.me.FavoritesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DbModule {

    @Provides
    @Singleton
    fun provideFavoriteRepository(appDatabase: AppDatabase): FavoritesRepository {
        return FavoritesRepositoryImpl(appDatabase.photoDao())
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "FavoritePhotos"
        ).build()
    }
}