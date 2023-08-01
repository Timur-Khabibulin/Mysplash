package com.timurkhabibulin.domain.user

import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.Result

interface UserRepository {

    suspend fun getUser(username: String): Result<User>

    suspend fun getUserPhotos(username: String, page: Int): Result<List<Photo>>

    suspend fun getUserLikedPhotos(username: String, page: Int): Result<List<Photo>>
}