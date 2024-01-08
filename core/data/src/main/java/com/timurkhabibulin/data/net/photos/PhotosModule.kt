package com.timurkhabibulin.data.net.photos

import com.timurkhabibulin.domain.photos.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface PhotosModule {

    @Binds
    fun bindPhotosRepository(
        photosRepositoryImpl: PhotosRepositoryImpl
    ): PhotosRepository

    companion object {
        @Singleton
        @Provides
        fun providePhotosApi(retrofit: Retrofit): PhotosApi {
            return retrofit.create(PhotosApi::class.java)
        }
    }
}