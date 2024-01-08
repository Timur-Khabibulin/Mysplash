package com.timurkhabibulin.data.net.search

import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.SearchResult
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.Result
import retrofit2.http.GET
import retrofit2.http.Query

internal interface SearchApi {

    @GET("/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("color") color: String?,
        @Query("orientation") orientation: String?
    ): Result<SearchResult<Photo>>

    @GET("/search/collections")
    suspend fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Result<SearchResult<Collection>>

    @GET("/search/users")
    suspend fun searchUsers(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Result<SearchResult<User>>
}