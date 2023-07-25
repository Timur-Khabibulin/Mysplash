package com.timurkhabibulin.data.photos

import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.entities.Photo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface PhotosApi {

    @GET("photos")
    suspend fun getPhotos(@Query("page") page: Int): Result<List<Photo>>

    @GET("photos/{id}")
    suspend fun getPhoto(@Path("id") id: String): Result<Photo>

}