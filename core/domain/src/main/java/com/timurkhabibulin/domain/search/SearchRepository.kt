package com.timurkhabibulin.domain.search

import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User

interface SearchRepository {

    suspend fun searchPhotos(
        query: String,
        page: Int,
        color: Color?,
        orientation: Orientation?
    ): com.timurkhabibulin.domain.result.Result<List<Photo>>

    suspend fun searchCollections(
        query: String,
        page: Int
    ): com.timurkhabibulin.domain.result.Result<List<Collection>>

    suspend fun searchUsers(
        query: String,
        page: Int
    ): com.timurkhabibulin.domain.result.Result<List<User>>
}