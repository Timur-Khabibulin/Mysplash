package com.timurkhabibulin.domain.photos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.timurkhabibulin.domain.ImageUtils
import com.timurkhabibulin.domain.ItemsPagingSource
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 10

class PhotosUseCase @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val dispatcher: CoroutineDispatcher,
    private val imageUtils: ImageUtils
) {

    fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                ItemsPagingSource(dispatcher = dispatcher) { page ->
                    photosRepository.getPhotos(page)
                }
            }
        ).flow.flowOn(dispatcher)
    }

    suspend fun getPhoto(id: String): Result<Photo> = withContext(dispatcher) {
        photosRepository.getPhoto(id)
    }

    suspend fun savePhoto(photo: Photo) = withContext(dispatcher) {
        imageUtils.download(photo, photo.urls.raw, photo.width, photo.height)
        photosRepository.trackDownload(photo.id)
    }

    suspend fun cropAndSetAsWallpaper(photo: Photo) = withContext(dispatcher) {
        imageUtils.cropAndSetAsWallpaper(photo, photo.urls.raw, photo.width, photo.height)
        photosRepository.trackDownload(photo.id)
    }
}
