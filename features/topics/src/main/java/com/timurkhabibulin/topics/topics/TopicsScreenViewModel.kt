package com.timurkhabibulin.topics.topics

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.result.asFailure
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import com.timurkhabibulin.domain.topics.TopicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TopicsScreenViewModel @Inject constructor(
    private val topicsUseCase: TopicsUseCase
) : ViewModel() {

    private val _topics = MutableStateFlow<List<Topic>?>(null)
    val topics: StateFlow<List<Topic>?> = _topics

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private val _photosByTopic: MutableMap<String, Flow<PagingData<Photo>>> = mutableMapOf()
    val photosByTopic: Map<String, Flow<PagingData<Photo>>> = _photosByTopic

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = topicsUseCase.getTopics()

            launch(Dispatchers.Main) {
                if (result.isSuccess()) {
                    result.asSuccess().value.forEach { topic ->
                        _photosByTopic[topic.id] =
                            topicsUseCase.getPhotos(topic.id).cachedIn(viewModelScope)

                    }
                    _topics.emit(result.asSuccess().value)
                } else  _errorMessage.value = result.asFailure().error?.message ?: ""
            }
        }
    }

    fun getIndexOfTopic(topicId: String): Int? {
        var topicIndex: Int? = null
        topics.value?.forEachIndexed { index, topic ->
            if (topic.id == topicId) topicIndex = index
        }
        return topicIndex
    }
}