package com.timurkhabibulin.data.user

import com.timurkhabibulin.data.NetworkModule
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import com.timurkhabibulin.domain.user.UserRepository
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getUser(username: String): Result<User> {
        val result = userApi.getUser(username)

        if (result.isSuccess()) {
            result.asSuccess().value.apply {
                links.html = "${links.html}${NetworkModule.UTM}"
            }
        }

        return result

    }

    override suspend fun getUserPhotos(username: String, page: Int): Result<List<Photo>> {
        return userApi.getUserPhotos(username, page)
    }

    override suspend fun getUserLikedPhotos(username: String, page: Int): Result<List<Photo>> {
        return userApi.getUserLikedPhotos(username, page)
    }
}
