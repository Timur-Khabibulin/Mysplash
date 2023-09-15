package com.timurkhabibulin.topics.photo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.timurkhabibulin.core.LoadState
import com.timurkhabibulin.core.isLoading
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosUseCase
import com.timurkhabibulin.domain.result.asFailure
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoScreenViewModel @Inject constructor(
    private val photosUseCase: PhotosUseCase,
    @ApplicationContext private val context: Context
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
            val bitmap = getDownloadedBitmap(photo)

            if (photosUseCase.savePhoto(
                    "${photo.user.username}-${photo.id}.jpeg",
                    bitmap
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

    private suspend fun getDownloadedBitmap(photo: Photo): Bitmap {

        val request = ImageRequest
            .Builder(context)
            .data(photo.urls.raw)
            .size(photo.width, photo.height)
            .build()

        val result = ImageLoader(context).execute(request)
        return (result.drawable as BitmapDrawable).bitmap
    }

}