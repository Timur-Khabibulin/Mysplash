package com.timurkhabibulin.data.user

import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface UserApi {

    @GET("/users/{username}")
    suspend fun getUser(@Path("username") username: String): Result<User>

    @GET("/users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String,
        @Query("page") page: Int
    ): Result<List<Photo>>

    @GET("/users/{username}/likes")
    suspend fun getUserLikedPhotos(
        @Path("username") username: String,
        @Query("page") page: Int
    ): Result<List<Photo>>
}