package com.timurkhabibulin.data

import com.timurkhabibulin.domain.DeviceStateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindDeviceStateRepository(impl: DeviceStateRepositoryImpl): DeviceStateRepository
}
