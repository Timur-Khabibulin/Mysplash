package com.timurkhabibulin.topics.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.photos.PhotosUseCase
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
    photosUseCase: PhotosUseCase,
    private val topicsUseCase: TopicsUseCase
) : ViewModel() {

    private val _topics = MutableStateFlow(listOf<Topic>())
    val topics: StateFlow<List<Topic>> = _topics

    val photos: Flow<PagingData<Photo>> = photosUseCase.getPhotos().cachedIn(viewModelScope)

    private val _photosByTopic: MutableMap<String, Flow<PagingData<Photo>>> = mutableMapOf()
    val photosByTopic: Map<String, Flow<PagingData<Photo>>> = _photosByTopic

    init {
        viewModelScope.launch(Dispatchers.IO) {
            topicsUseCase.getTopics().let {
                it.forEach { topic ->
                    _photosByTopic[topic.id] =
                        topicsUseCase.getPhotos(topic.id).cachedIn(viewModelScope)
                }
                _topics.emit(it)
            }
        }
    }
}