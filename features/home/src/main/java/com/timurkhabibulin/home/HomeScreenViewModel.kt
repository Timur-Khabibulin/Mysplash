package com.timurkhabibulin.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.photos.PhotosUseCase
import com.timurkhabibulin.domain.result.onSuccess
import com.timurkhabibulin.domain.topics.TopicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeScreenViewModel @Inject constructor(
    photosUseCase: PhotosUseCase,
    private val topicsUseCase: TopicsUseCase
) : ViewModel() {

    private val _topics = mutableStateOf(listOf<Topic>())
    val topics: State<List<Topic>> = _topics

    val photos: Flow<PagingData<Photo>> = photosUseCase.getPhotos().cachedIn(viewModelScope)

    init {
        loadTopics()
    }

    private fun loadTopics() {
        viewModelScope.launch {
            topicsUseCase.getTopics().onSuccess {
                _topics.value = it
            }
        }
    }

}