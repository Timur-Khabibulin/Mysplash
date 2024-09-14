package com.timurkhabibulin.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.DeviceStateRepository
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.search.SearchUseCase
import com.timurkhabibulin.search.filter.FilterHandler
import com.timurkhabibulin.search.filter.FilterUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val deviceStateRepository: DeviceStateRepository
) : ViewModel() {

    private var loadJob: Job? = null

    private val _filters = MutableStateFlow(SearchScreenFiltersState())
    val filters = _filters.asStateFlow()

    private val _photos = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val photos = _photos.asStateFlow()

    private val _collections = MutableStateFlow<PagingData<Collection>>(PagingData.empty())
    val collections = _collections.asStateFlow()

    private val _users = MutableStateFlow<PagingData<User>>(PagingData.empty())
    val users = _users.asStateFlow()

    val filterHandler = FilterHandler()

    init {
        viewModelScope.launch {
            deviceStateRepository.networkAvailableState()
                .distinctUntilChanged()
                .onEach {
                    startSearch()
                }.stateIn(viewModelScope)
        }
    }

    fun updateFilterStateAndStartSearch(state: FilterUIState) {
        _filters.update {
            it.copy(filters = state)
        }
        startSearch()
    }

    fun updateCategory(category: SearchCategory) {
        _filters.update {
            it.copy(category = category)
        }
    }

    fun updateQuery(query: String) {
        _filters.update {
            it.copy(query = query)
        }
    }

    fun startSearch() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            launch {
                searchUseCase.searchPhotos(
                    query = filters.value.query,
                    color = filters.value.filters.color,
                    orientation = filters.value.filters.orientation
                )
                    .cachedIn(viewModelScope)
                    .collect {
                        _photos.value = it
                    }
            }
            launch {
                searchUseCase.searchCollections(filters.value.query)
                    .cachedIn(viewModelScope)
                    .collect {
                        _collections.value = it
                    }
            }
            launch {
                searchUseCase.searchUsers(filters.value.query)
                    .cachedIn(viewModelScope)
                    .collect {
                        _users.value = it
                    }
            }
        }
    }
}
