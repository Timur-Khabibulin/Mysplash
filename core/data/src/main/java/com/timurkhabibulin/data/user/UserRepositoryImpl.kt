package com.timurkhabibulin.data.user

import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.user.UserRepository
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getUser(username: String): Result<User> {
        return userApi.getUser(username)
    }

    override suspend fun getUserPhotos(username: String, page: Int): Result<List<Photo>> {
        return userApi.getUserPhotos(username, page)
    }

    override suspend fun getUserLikedPhotos(username: String, page: Int): Result<List<Photo>> {
        return userApi.getUserLikedPhotos(username, page)
    }
}