package com.timurkhabibulin.data.search

import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.search.SearchRepository
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi
) : SearchRepository {
    override suspend fun searchPhotos(
        query: String,
        page: Int,
        color: Color?,
        orientation: Orientation?
    ): Result<List<Photo>> {
        return searchApi.searchPhotos(query, page, color, orientation)
    }

    override suspend fun searchCollections(query: String, page: Int): Result<List<Collection>> {
        return searchApi.searchCollections(query, page)
    }

    override suspend fun searchUsers(query: String, page: Int): Result<List<User>> {
        return searchApi.searchUsers(query, page)
    }
}