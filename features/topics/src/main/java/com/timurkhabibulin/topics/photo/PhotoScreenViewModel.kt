package com.timurkhabibulin.topics.photo

import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.core.BaseViewModel
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoScreenViewModel @Inject constructor(
    private val photosUseCase: PhotosUseCase
) : BaseViewModel<Photo>() {

    fun loadPhoto(id: String) {
        loadData {
            photosUseCase.getPhoto(id)
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
