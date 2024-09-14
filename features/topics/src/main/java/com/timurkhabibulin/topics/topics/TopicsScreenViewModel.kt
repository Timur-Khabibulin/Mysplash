package com.timurkhabibulin.topics.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.DeviceStateRepository
import com.timurkhabibulin.domain.LoadState
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.onSuccess
import com.timurkhabibulin.domain.result.onFailure
import com.timurkhabibulin.domain.result.onSuccess
import com.timurkhabibulin.domain.topics.TopicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TopicsScreenViewModel @Inject constructor(
    private val topicsUseCase: TopicsUseCase,
    private val deviceStateRepository: DeviceStateRepository
) : ViewModel() {
    private var loadJob: Job? = null

    private val _topics = MutableStateFlow<LoadState<List<Topic>>>(LoadState.Loading())
    val topics: StateFlow<LoadState<List<Topic>>> = _topics

    private val _photosByTopic: MutableMap<String, Flow<PagingData<Photo>>> = mutableMapOf()
    val photosByTopic: Map<String, Flow<PagingData<Photo>>> = _photosByTopic

    init {
        loadTopics()
        viewModelScope.launch {
            deviceStateRepository.networkAvailableState()
                .distinctUntilChanged()
                .onEach {
                    loadTopics()
                }.stateIn(viewModelScope)
        }
    }

    fun getIndexOfTopic(topicId: String): Int? {
        _topics.value.onSuccess {
            it.forEachIndexed { index, topic ->
                if (topic.id == topicId) return index
            }
        }
        return null
    }

    private fun loadTopics() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            topicsUseCase.getTopics()
                .onSuccess {
                    _topics.emit(LoadState.Completed.Success(it))
                    it.bindPhotosToTopics()
                }.onFailure {
                    _topics.emit(LoadState.Completed.Failure(it))
                }
        }
    }

    private fun List<Topic>.bindPhotosToTopics() {
        forEach { topic ->
            _photosByTopic[topic.id] =
                topicsUseCase.getPhotos(topic.id).cachedIn(viewModelScope)
        }
    }
}
