package com.timurkhabibulin.topics.photo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosUseCase
import com.timurkhabibulin.domain.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoScreenViewModel @Inject constructor(
    private val photosUseCase: PhotosUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val photoID = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val photo: Flow<Result<Photo>> = photoID
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id -> photosUseCase.getPhoto(id) }

    fun loadPhoto(id: String) {
        photoID.value = id
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