package com.timurkhabibulin.core.di


import com.timurkhabibulin.domain.ImageUtils
import com.timurkhabibulin.core.utils.ImageUtilsImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface CoreModule {
    companion object {
        @Singleton
        @Provides
        fun providesCoroutineScope(): CoroutineScope {
            return CoroutineScope(SupervisorJob() + Dispatchers.Default)
        }

        @Singleton
        @Provides
        fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO
    }


    @Binds
    fun bindImageDownloader(imageDownloaderImpl: ImageUtilsImpl): ImageUtils
}
