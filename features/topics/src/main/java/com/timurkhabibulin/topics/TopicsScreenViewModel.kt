package com.timurkhabibulin.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosUseCase
import com.timurkhabibulin.domain.topics.TopicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TopicsScreenViewModel @Inject constructor(
   private val photosUseCase: PhotosUseCase,
    private val topicsUseCase: TopicsUseCase
) : ViewModel() {

    val topics = topicsUseCase.getTopics()

    fun getPhotos(): Flow<PagingData<Photo>>{
        return photosUseCase.getPhotos().cachedIn(viewModelScope)
    }
    fun getPhotosFromTopic(topicId: String): Flow<PagingData<Photo>> {
        return topicsUseCase.getPhotos(topicId).cachedIn(viewModelScope)
    }
}