package com.timurkhabibulin.data.photos


import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.photos.PhotosRepository
import com.timurkhabibulin.domain.entities.Photo
import javax.inject.Inject

internal class PhotosRepositoryImpl @Inject constructor(
    private val photosApi: PhotosApi
) : PhotosRepository {
    override suspend fun getPhotos(page: Int): Result<List<Photo>> {
        return photosApi.getPhotos(page)
    }

    override suspend fun getPhoto(id: String): Result<Photo> {
        return photosApi.getPhoto(id)
    }

    override suspend fun trackDownload(id: String) {
        photosApi.trackDownload(id)
    }

    override suspend fun downloadPhoto(url: String): Result<Photo> {
        return photosApi.downloadPhoto(url)
    }
}