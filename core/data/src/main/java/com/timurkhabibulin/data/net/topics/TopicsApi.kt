package com.timurkhabibulin.data.net.topics

import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface TopicsApi {

    @GET("topics?per_page=50")
    suspend fun getTopics(): Result<List<Topic>>

    @GET("topics/{id_or_slug}/photos")
    suspend fun getTopicPhotos(
        @Path("id_or_slug") id: String,
        @Query("page") page: Int
    ): Result<List<Photo>>
}