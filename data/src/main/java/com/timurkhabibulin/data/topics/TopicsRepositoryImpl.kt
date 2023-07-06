package com.timurkhabibulin.data.topics

import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.topics.TopicsRepository
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import javax.inject.Inject

internal class TopicsRepositoryImpl @Inject constructor(
    private val topicsApi: TopicsApi
) : TopicsRepository {
    override suspend fun getTopics(): Result<List<Topic>> {
        return topicsApi.getTopics()
    }

    override suspend fun getTopicPhotos(topicId: String, page: Int): Result<List<Photo>> {
        return topicsApi.getTopicPhotos(topicId, page)
    }
}