package com.timurkhabibulin.data.net.collections

import com.timurkhabibulin.domain.collections.CollectionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CollectionsModule {

    @Provides
    fun provideCollectionsRepo(collectionsApi: CollectionsApi): CollectionsRepository {
        return CollectionsRepositoryImpl(collectionsApi)
    }

    @Singleton
    @Provides
    fun provideCollectionApi(retrofit: Retrofit): CollectionsApi {
        return retrofit.create(CollectionsApi::class.java)
    }
}