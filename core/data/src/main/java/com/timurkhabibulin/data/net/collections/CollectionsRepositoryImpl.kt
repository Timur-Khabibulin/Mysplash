package com.timurkhabibulin.data.net.collections

import com.timurkhabibulin.domain.collections.CollectionsRepository
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.result.Result
import javax.inject.Inject

internal class CollectionsRepositoryImpl @Inject constructor(
    private val collectionsApi: CollectionsApi
) : CollectionsRepository {
    override suspend fun getUserCollections(username: String, page: Int): Result<List<Collection>> {
        return collectionsApi.getUserCollections(username, page)
    }

    override suspend fun getCollectionPhotos(id: String, page: Int): Result<List<Photo>> {
        return collectionsApi.getCollectionPhotos(id, page)
    }

    override suspend fun getCollection(id: String): Result<Collection> {
        return collectionsApi.getCollection(id)
    }
}