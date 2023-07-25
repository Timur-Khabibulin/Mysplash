package com.timurkhabibulin.topics.impl.ui.photo

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.photos.PhotosUseCase
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
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class PhotoScreenViewModel @Inject constructor(
    photosUseCase: PhotosUseCase,
    @ApplicationContext private val context: Context
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

    fun downloadPhoto(photo: Photo) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = ImageRequest
                .Builder(context)
                .data(photo.links.download)
                .build()

            val result = ImageLoader(context).execute(request).drawable
            val bitmap = (result as BitmapDrawable).bitmap

            savePhoto("${photo.user.username}-${photo.id}.jpeg", bitmap)
        }
    }

    private fun savePhoto(fileName: String, bitmap: Bitmap) {
        val resolver = context.contentResolver
        var outputStream: OutputStream?

        if (Build.VERSION.SDK_INT > +Build.VERSION_CODES.Q) {
            context.contentResolver.also {

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.TITLE, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                outputStream =
                    resolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )?.let {
                        resolver.openOutputStream(it)
                    }
            }

            outputStream?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }

        }
    }
}