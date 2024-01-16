package com.timurkhabibulin.mysplash.di

import com.timurkhabibulin.core.analytics.Analytics
import com.timurkhabibulin.mysplash.analytics.AnalyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface AppModule {
    @Binds
    @Singleton
    fun bindAnalytics(analyticsImpl: AnalyticsImpl): Analytics
}