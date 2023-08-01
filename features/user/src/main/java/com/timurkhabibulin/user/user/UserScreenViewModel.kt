package com.timurkhabibulin.user.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.collections.CollectionsUseCase
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
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

    private val _user = MutableStateFlow(User.DefaultUser)
    val user: StateFlow<User> = _user

    @OptIn(ExperimentalCoroutinesApi::class)
    val userPhotos: Flow<PagingData<Photo>> = user
        .filterNotNull()
        .flatMapLatest {
            userUseCase.getUserPhotos(it.username).cachedIn(viewModelScope)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userLikedPhotos: Flow<PagingData<Photo>> = user
        .filterNotNull()
        .flatMapLatest { userUseCase.getUserLikedPhotos(it.username) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val userCollections: Flow<PagingData<Collection>> = user
        .filterNotNull()
        .flatMapLatest { collectionsUseCase.getUserCollections(it.username) }
        .cachedIn(viewModelScope)

    fun loadUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.emit(userUseCase.getUser(username))
        }
    }
}