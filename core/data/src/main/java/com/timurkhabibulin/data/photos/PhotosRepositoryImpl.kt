package com.timurkhabibulin.data.photos


import com.timurkhabibulin.data.attribution.AttributionRepository
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosRepository
import com.timurkhabibulin.domain.result.Result
import javax.inject.Inject

internal class PhotosRepositoryImpl @Inject constructor(
    private val photosApi: PhotosApi,
    private val attributionRepository: AttributionRepository
) : PhotosRepository {
    override suspend fun getPhotos(page: Int): Result<List<Photo>> {
        return photosApi.getPhotos(page)
    }

    override suspend fun getPhoto(id: String): Result<Photo> {
        val result = photosApi.getPhoto(id)

/*        if (result.isSuccess()) {
            result.asSuccess().value.links.html?.let {
                attributionRepository.attribute(it)
            }
        }*/

        return result
    }

    override suspend fun trackDownload(id: String) {
        photosApi.trackDownload(id)
    }

    override suspend fun downloadPhoto(url: String): Result<Photo> {
        return photosApi.downloadPhoto(url)
    }
}