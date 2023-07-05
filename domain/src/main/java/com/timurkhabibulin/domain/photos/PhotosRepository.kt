package com.timurkhabibulin.domain.photos

import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.entities.Photo

interface PhotosRepository {
    suspend fun getPhotos(page: Int): Result<List<Photo>>
}