package com.timurkhabibulin.domain.photos

import android.graphics.Bitmap
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.timurkhabibulin.core.StorageService
import com.timurkhabibulin.domain.ItemsPagingSource
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 10

class PhotosUseCase @Inject constructor(
    private val photosRepository: PhotosRepository,
    private val dispatcher: CoroutineDispatcher,
    private val storageService: StorageService
) {

    fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    photosRepository.getPhotos(page)
                }
            }
        ).flow.flowOn(dispatcher)
    }

    fun getPhoto(id: String): Flow<Photo> = flow {
        val result = photosRepository.getPhoto(id)
        emit(
            if (result.isSuccess()) result.asSuccess().value
            else Photo.Default
        )
    }

    suspend fun trackDownload(id: String) {
        photosRepository.trackDownload(id)
    }

    suspend fun downloadPhoto(url: String): Result<Photo> {
        return photosRepository.downloadPhoto(url)
    }

    fun savePhoto(fileName: String, bitmap: Bitmap): Boolean {
        return storageService.saveBitmap(fileName, bitmap)
    }
}