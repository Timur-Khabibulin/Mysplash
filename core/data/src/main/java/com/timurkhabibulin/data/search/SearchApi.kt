package com.timurkhabibulin.data.search

import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.Result
import retrofit2.http.GET
import retrofit2.http.Query

internal interface SearchApi {

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("color") color: Color?,
        @Query("orientation") orientation: Orientation?
    ): Result<List<Photo>>

    @GET("/search/collections")
    suspend fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Result<List<Collection>>

    @GET("/search/users")
    suspend fun searchUsers(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Result<List<User>>
}