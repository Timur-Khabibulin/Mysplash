package com.timurkhabibulin.user.user

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.core.BaseViewModel
import com.timurkhabibulin.core.LoadState
import com.timurkhabibulin.core.asSuccessfulCompletion
import com.timurkhabibulin.core.isSuccessfulCompletion
import com.timurkhabibulin.domain.collections.CollectionsUseCase
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
internal class UserScreenViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val collectionsUseCase: CollectionsUseCase
) : BaseViewModel<User>() {

    val userPhotos: Flow<PagingData<Photo>> = state.makeRequest {
        userUseCase.getUserPhotos(it.username)
    }

    val userLikedPhotos: Flow<PagingData<Photo>> = state.makeRequest {
        userUseCase.getUserLikedPhotos(it.username)
    }

    val userCollections: Flow<PagingData<Collection>> = state.makeRequest {
        collectionsUseCase.getUserCollections(it.username)
    }

    fun loadUser(username: String) {
        loadData {
            userUseCase.getUser(username)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T : Any> StateFlow<LoadState<User>>.makeRequest(request: (User) -> Flow<PagingData<T>>): Flow<PagingData<T>> {
        return this.filter { it.isSuccessfulCompletion() }
            .flatMapLatest { request(it.asSuccessfulCompletion().value) }
            .cachedIn(viewModelScope)
    }
}
