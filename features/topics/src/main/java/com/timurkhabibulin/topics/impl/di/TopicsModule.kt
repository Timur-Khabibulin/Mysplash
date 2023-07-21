package com.timurkhabibulin.topics.impl.di

import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.topics.impl.TopicsApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
class TopicsModule {

    @Provides
    @IntoSet
    fun provideTopicsApi(): FeatureApi = TopicsApiImpl()
}