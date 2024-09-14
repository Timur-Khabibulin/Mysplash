package com.timurkhabibulin.user.favorites

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.me.FavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoritesUseCase: FavoritesUseCase
) : ViewModel() {
    val state: Flow<PagingData<Photo>> = favoritesUseCase.getFavoritesPhotos()
}