package com.timurkhabibulin.domain.user

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.timurkhabibulin.domain.ItemsPagingSource
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 10

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getUser(username: String): User {
        val result = userRepository.getUser(username)
        return if (result.isSuccess()) result.asSuccess().value
        else User.DefaultUser
    }

    fun getUserPhotos(username: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    userRepository.getUserPhotos(username, page)
                }
            }
        ).flow.flowOn(dispatcher)
    }

    fun getUserLikedPhotos(username: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                ItemsPagingSource { page ->
                    userRepository.getUserLikedPhotos(username, page)
                }
            }
        ).flow.flowOn(dispatcher)
    }

}