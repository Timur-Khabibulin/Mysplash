package com.timurkhabibulin.user.user

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.collections.CollectionsUseCase
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.asFailure
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import com.timurkhabibulin.domain.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class UserScreenViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val collectionsUseCase: CollectionsUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    val userPhotos: Flow<PagingData<Photo>> = user.makeRequest {
        userUseCase.getUserPhotos(it.username)
    }

    val userLikedPhotos: Flow<PagingData<Photo>> = user.makeRequest {
        userUseCase.getUserLikedPhotos(it.username)
    }

    val userCollections: Flow<PagingData<Collection>> = user.makeRequest {
        collectionsUseCase.getUserCollections(it.username)
    }

    fun loadUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userUseCase.getUser(username)

            launch(Dispatchers.Main) {
                if (result.isSuccess())
                    _user.value = result.asSuccess().value
                else _errorMessage.value = result.asFailure().error?.message ?: ""
            }

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T : Any> StateFlow<User?>.makeRequest(request: (User) -> Flow<PagingData<T>>): Flow<PagingData<T>> {
        return this.filterNotNull()
            .flatMapLatest { request(it) }
            .cachedIn(viewModelScope)
    }
}