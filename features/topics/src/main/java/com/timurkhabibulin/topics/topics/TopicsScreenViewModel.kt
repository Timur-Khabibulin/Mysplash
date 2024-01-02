package com.timurkhabibulin.topics.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.core.LoadState
import com.timurkhabibulin.core.isLoading
import com.timurkhabibulin.core.onSuccess
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.result.onFailure
import com.timurkhabibulin.domain.result.onSuccess
import com.timurkhabibulin.domain.topics.TopicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TopicsScreenViewModel @Inject constructor(
    private val topicsUseCase: TopicsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoadState<List<Topic>>>(LoadState.Loading())
    val state: StateFlow<LoadState<List<Topic>>> = _state

    private val _photosByTopic: MutableMap<String, Flow<PagingData<Photo>>> = mutableMapOf()
    val photosByTopic: Map<String, Flow<PagingData<Photo>>> = _photosByTopic

    init {
        viewModelScope.launch {
            if (_state.value.isLoading()) {
                topicsUseCase.getTopics()
                    .onSuccess {
                        _state.emit(LoadState.Completed.Success(it))
                        it.bindPhotosToTopics()
                    }.onFailure {
                        _state.emit(LoadState.Completed.Failure(it))
                    }
            }
        }
    }

    fun getIndexOfTopic(topicId: String): Int? {
        _state.value.onSuccess {
            it.forEachIndexed { index, topic ->
                if (topic.id == topicId) return index
            }
        }
        return null
    }

    private fun List<Topic>.bindPhotosToTopics() {
        forEach { topic ->
            _photosByTopic[topic.id] =
                topicsUseCase.getPhotos(topic.id).cachedIn(viewModelScope)
        }
    }
}