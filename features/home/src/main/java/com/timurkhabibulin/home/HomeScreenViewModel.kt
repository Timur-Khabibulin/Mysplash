package com.timurkhabibulin.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.timurkhabibulin.domain.DeviceStateRepository
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.photos.PhotosUseCase
import com.timurkhabibulin.domain.result.onSuccess
import com.timurkhabibulin.domain.topics.TopicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeScreenViewModel @Inject constructor(
    private val photosUseCase: PhotosUseCase,
    private val topicsUseCase: TopicsUseCase,
    private val deviceStateRepository: DeviceStateRepository
) : ViewModel() {

    private var loadJob: Job? = null

    private val _topics = MutableStateFlow<List<Topic>>(listOf())
    val topics = _topics.asStateFlow()

    private val _photos = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val photos = _photos.asStateFlow()

    init {
        load()
        viewModelScope.launch {
            deviceStateRepository.networkAvailableState()
                .distinctUntilChanged()
                .onEach {
                    load()
                }.stateIn(viewModelScope)
        }
    }

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            launch {
                topicsUseCase.getTopics().onSuccess {
                    _topics.value = it
                }
            }
            launch {
                photosUseCase.getPhotos().collect {
                    _photos.value = it
                }
            }
        }
    }
}
