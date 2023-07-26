package com.timurkhabibulin.data.photos

import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.result.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

internal interface PhotosApi {

    @GET("photos")
    suspend fun getPhotos(@Query("page") page: Int): Result<List<Photo>>

    @GET("photos/{id}")
    suspend fun getPhoto(@Path("id") id: String): Result<Photo>

    @GET("photos/{id}/download")
    suspend fun trackDownload(@Path("id") id: String)

    @GET
    suspend fun downloadPhoto(@Url url: String): Result<Photo>
}