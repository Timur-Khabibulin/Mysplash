package com.timurkhabibulin.domain.me

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val photosRepository: PhotosRepository,
    private val dispatcher: CoroutineDispatcher
) {
    fun getFavoritesPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = FavoritesPagingSource.PAGE_SIZE),
            pagingSourceFactory = {
                FavoritesPagingSource(favoritesRepository, photosRepository, dispatcher)
            }
        ).flow.flowOn(dispatcher)
    }

    suspend fun isFavorite(id: String): Boolean = withContext(dispatcher) {
        favoritesRepository.isFavorite(id)
    }

    suspend fun addToFavorite(id: String) = withContext(dispatcher) {
        favoritesRepository.addToFavorite(id)
    }

    suspend fun removeFromFavorite(id: String) = withContext(dispatcher) {
        favoritesRepository.removeFromFavorite(id)
    }
}