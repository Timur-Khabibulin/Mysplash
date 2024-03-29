package com.timurkhabibulin.domain.me

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosRepository
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class FavoritesPagingSource(
    private val favoritesRepository: FavoritesRepository,
    private val photosRepository: PhotosRepository,
    private val dispatcher: CoroutineDispatcher,
) : PagingSource<Int, Photo>() {

    companion object {
        const val START_PAGE_INDEX = 0
        const val PAGE_SIZE = 10
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(-1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> =
        withContext(dispatcher) {
            val page = params.key ?: START_PAGE_INDEX
            val favorites = favoritesRepository.getFavoritePhotos(
                PAGE_SIZE,
                page * PAGE_SIZE
            )

            if (favorites.isEmpty() && page == 0)
                LoadResult.Error<Int, Photo>(Throwable())

            val photos = favorites.map {
                photosRepository.getPhoto(it)
            }.filter {
                it.isSuccess()
            }.map {
                it.asSuccess().value
            }

            LoadResult.Page(
                data = photos,
                prevKey = if (page == START_PAGE_INDEX) null else page - 1,
                nextKey = if (photos.isEmpty()) null else page + 1
            )
        }
}