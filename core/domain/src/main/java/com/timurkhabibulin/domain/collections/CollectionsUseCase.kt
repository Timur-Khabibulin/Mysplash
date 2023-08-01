package com.timurkhabibulin.domain.collections

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.timurkhabibulin.domain.ItemsPagingSource
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 10

class CollectionsUseCase @Inject constructor(
    private val collectionsRepository: CollectionsRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getCollection(id: String): Collection? {
        val result = collectionsRepository.getCollection(id)
        return if (result.isSuccess()) result.asSuccess().value
        else null
    }

    fun getUserCollections(username: String): Flow<PagingData<Collection>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    collectionsRepository.getUserCollections(username, page)
                }
            }
        ).flow.flowOn(dispatcher)
    }

    fun getCollectionPhotos(id: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    collectionsRepository.getCollectionPhotos(id, page)
                }
            }
        ).flow.flowOn(dispatcher)
    }
}