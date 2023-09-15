package com.timurkhabibulin.data.attribution

import retrofit2.http.GET
import retrofit2.http.Url


internal interface RequestApi {
    @GET
    suspend fun makeRequest(@Url url: String)
}