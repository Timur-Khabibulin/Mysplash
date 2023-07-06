package com.timurkhabibulin.data.topics

import com.timurkhabibulin.domain.topics.TopicsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal class TopicsModule {

    @Provides
    fun provideTopicsRepository(topicsApi: TopicsApi): TopicsRepository {
        return TopicsRepositoryImpl(topicsApi)
    }

    @Provides
    fun provideTopicsApi(retrofit: Retrofit): TopicsApi {
        return retrofit.create(TopicsApi::class.java)
    }
}