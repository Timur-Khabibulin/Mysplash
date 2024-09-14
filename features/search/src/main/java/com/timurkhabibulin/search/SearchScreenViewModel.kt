package com.timurkhabibulin.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

private const val SEARCH_QUERY = "search_query"

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _searchParametersChanged = MutableStateFlow(false)

    val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY, "")
    val color: MutableState<Color?> = mutableStateOf(null)
    val orientation: MutableState<Orientation?> = mutableStateOf(null)

    val photos: Flow<PagingData<Photo>> = _searchParametersChanged.getDataFromRequest {
        searchUseCase.searchPhotos(searchQuery.value, color.value, orientation.value)
    }

    val collections: Flow<PagingData<Collection>> = _searchParametersChanged.getDataFromRequest {
        searchUseCase.searchCollections(searchQuery.value)
    }

    val users: Flow<PagingData<User>> = _searchParametersChanged.getDataFromRequest {
        searchUseCase.searchUsers(searchQuery.value)
    }

    fun searchParametersChanged() {
        _searchParametersChanged.value = true
    }

    fun changeSearchQuery(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
        searchParametersChanged()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T : Any> MutableStateFlow<Boolean>.getDataFromRequest(request: () -> Flow<PagingData<T>>): Flow<PagingData<T>> {
        return this.flatMapLatest {
            val result = request()
            _searchParametersChanged.value = false
            return@flatMapLatest result
        }.cachedIn(viewModelScope)
    }
}