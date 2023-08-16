package com.timurkhabibulin.domain.search

import android.os.Parcelable
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.timurkhabibulin.domain.ItemsPagingSource
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.SearchResult
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.result.asFailure
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 10

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    fun searchPhotos(
        query: String,
        color: Color?,
        orientation: Orientation?
    ): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    searchRepository.searchPhotos(query, page, color, orientation)
                        .withoutSearchResult()
                }
            }
        ).flow.flowOn(dispatcher)
    }

    fun searchCollections(
        query: String
    ): Flow<PagingData<Collection>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    searchRepository.searchCollections(query, page).withoutSearchResult()
                }
            }
        ).flow.flowOn(dispatcher)
    }

    fun searchUsers(
        query: String
    ): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    searchRepository.searchUsers(query, page).withoutSearchResult()
                }
            }
        ).flow.flowOn(dispatcher)
    }

    private fun <T : Parcelable> Result<SearchResult<T>>.withoutSearchResult(): Result<List<T>> {
        return if (this.isSuccess()) Result.Success.HttpResponse(
            value = this.asSuccess().value.results,
            200
        )
        else Result.Failure.Error(this.asFailure().error!!)
    }
}