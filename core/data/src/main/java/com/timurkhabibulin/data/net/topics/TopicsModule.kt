package com.timurkhabibulin.data.net.topics

import com.timurkhabibulin.domain.topics.TopicsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class TopicsModule {

    @Provides
    fun provideTopicsRepository(topicsApi: TopicsApi): TopicsRepository {
        return TopicsRepositoryImpl(topicsApi)
    }

    @Provides
    @Singleton
    fun provideTopicsApi(retrofit: Retrofit): TopicsApi {
        return retrofit.create(TopicsApi::class.java)
    }
}