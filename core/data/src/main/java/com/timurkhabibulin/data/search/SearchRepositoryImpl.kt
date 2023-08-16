package com.timurkhabibulin.data.search

import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.SearchResult
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
    ): Result<SearchResult<Photo>> {
        return searchApi.searchPhotos(query, page, color?.colorName, orientation?.orientationName)
    }

    override suspend fun searchCollections(
        query: String,
        page: Int
    ): Result<SearchResult<Collection>> {
        return searchApi.searchCollections(query, page)
    }

    override suspend fun searchUsers(query: String, page: Int): Result<SearchResult<User>> {
        return searchApi.searchUsers(query, page)
    }

}