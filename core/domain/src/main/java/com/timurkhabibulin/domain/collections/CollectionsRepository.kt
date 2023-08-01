package com.timurkhabibulin.domain.collections

import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.result.Result

interface CollectionsRepository {

    suspend fun getUserCollections(username: String, page: Int): Result<List<Collection>>

    suspend fun getCollectionPhotos(id: String, page: Int): Result<List<Photo>>

    suspend fun getCollection(id: String): Result<Collection>
}