package com.timurkhabibulin.topics.photo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.core.BaseViewModel
import com.timurkhabibulin.domain.asSuccessfulCompletion
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.me.FavoritesUseCase
import com.timurkhabibulin.domain.photos.PhotosUseCase
import com.timurkhabibulin.domain.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoScreenViewModel @Inject constructor(
    private val photosUseCase: PhotosUseCase,
    private val favoritesUseCase: FavoritesUseCase
) : BaseViewModel<Photo>() {
    private val _isFavorite = mutableStateOf(false)
    val isFavorite: State<Boolean> = _isFavorite

    fun loadPhoto(id: String) {
        loadData {
            photosUseCase.getPhoto(id).onSuccess {
                _isFavorite.value = favoritesUseCase.isFavorite(it.id)
            }
        }
    }

    fun downloadPhoto(photo: Photo) {
        viewModelScope.launch {
            photosUseCase.savePhoto(photo)
        }
    }

    fun onLikeClick(id: String) {
        viewModelScope.launch {
            if (favoritesUseCase.isFavorite(id)) {
                favoritesUseCase.removeFromFavorite(id)
                _isFavorite.value = false
            } else {
                favoritesUseCase.addToFavorite(id)
                _isFavorite.value = true
            }
        }
    }

    fun setAsWallpaper() {
        viewModelScope.launch {
            val photo = state.value.asSuccessfulCompletion().value
            photosUseCase.cropAndSetAsWallpaper(photo)
        }
    }
}
