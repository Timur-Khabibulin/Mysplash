package com.timurkhabibulin.data.net.collections

import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.result.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CollectionsApi {

    @GET("/users/{username}/collections")
    suspend fun getUserCollections(
        @Path("username") username: String,
        @Query("page") page: Int
    ): Result<List<Collection>>

    @GET("/collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") id: String,
        @Query("page") page: Int
    ): Result<List<Photo>>

    @GET("/collections/{id}")
    suspend fun getCollection(
        @Path("id") id: String
    ): Result<Collection>
}