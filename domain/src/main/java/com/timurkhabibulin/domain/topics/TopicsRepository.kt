package com.timurkhabibulin.domain.topics

import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic

interface TopicsRepository {
    suspend fun getTopics(): Result<List<Topic>>

    suspend fun getTopicPhotos(topicId: String, page: Int): Result<List<Photo>>
}