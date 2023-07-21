package com.timurkhabibulin.topics.impl.ui.photo

import androidx.lifecycle.ViewModel
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class PhotoScreenViewModel @Inject constructor(
    photosUseCase: PhotosUseCase
) : ViewModel() {
    private val photoID = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val photo: Flow<Photo> = photoID
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id -> photosUseCase.getPhoto(id) }

    fun loadPhoto(id: String) {
        photoID.value = id
    }
}