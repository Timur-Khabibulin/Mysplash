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
}