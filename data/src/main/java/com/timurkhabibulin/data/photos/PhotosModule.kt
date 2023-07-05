package com.timurkhabibulin.data.photos

import com.timurkhabibulin.domain.photos.PhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PhotosModule {
    @Provides
    fun providePhotosRepo(photosApi: PhotosApi): PhotosRepository {
        return PhotosRepositoryImpl(photosApi)
    }

    @Singleton
    @Provides
    fun providePhotosApi(retrofit: Retrofit): PhotosApi {
        return retrofit.create(PhotosApi::class.java)
    }
}