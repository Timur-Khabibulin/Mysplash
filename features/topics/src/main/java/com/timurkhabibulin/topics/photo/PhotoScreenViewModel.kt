package com.timurkhabibulin.topics.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.core.LoadState
import com.timurkhabibulin.core.isLoading
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosUseCase
import com.timurkhabibulin.domain.result.asFailure
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoScreenViewModel @Inject constructor(
    private val photosUseCase: PhotosUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<LoadState<Photo>> = MutableStateFlow(LoadState.Loading())
    val state: StateFlow<LoadState<Photo>> = _state

    fun loadPhoto(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_state.value.isLoading()) {
                val result = photosUseCase.getPhoto(id)

                _state.emit(
                    if (result.isSuccess()) {
                        LoadState.Completed.Success(result.asSuccess().value)
                    } else {
                        LoadState.Completed.Failure(result.asFailure().error)
                    }
                )
            }
        }
    }

    fun downloadPhoto(photo: Photo, onStartDownload: () -> Unit, onDownloadComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                onStartDownload()
            }

            if (photosUseCase.savePhoto(
                    "${photo.user.username}-${photo.id}.jpeg",
                    photo.urls.raw,
                    photo.width,
                    photo.height
                )
            ) {
                photo.links.download_location?.let {
                    photosUseCase.downloadPhoto(it)
                }
                photosUseCase.trackDownload(photo.id)
                launch(Dispatchers.Main) {
                    onDownloadComplete()
                }
            }
        }
    }

}