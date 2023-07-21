package com.timurkhabibulin.domain.topics

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.timurkhabibulin.domain.ItemsPagingSource
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 10

class TopicsUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun getTopics(): List<Topic> {
        val result = topicsRepository.getTopics()
        return if (result.isSuccess()) topicsRepository.getTopics().asSuccess().value
        else listOf()
    }

    fun getPhotos(topicId: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    topicsRepository.getTopicPhotos(topicId, page)
                }
            }
        ).flow.flowOn(dispatcher)
    }
}