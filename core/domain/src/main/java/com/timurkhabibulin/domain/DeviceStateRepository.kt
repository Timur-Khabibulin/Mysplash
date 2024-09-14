package com.timurkhabibulin.domain

import kotlinx.coroutines.flow.Flow


interface DeviceStateRepository {

    suspend fun isNetworkAvailable(): Boolean
    fun networkAvailableState(): Flow<Boolean>
}
