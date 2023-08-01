package com.timurkhabibulin.user.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.domain.collections.CollectionsUseCase
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CollectionPhotosScreenViewModel @Inject constructor(
    private val collectionsUseCase: CollectionsUseCase
) : ViewModel() {

    private val _collection = MutableStateFlow<Collection?>(null)
    val collection: StateFlow<Collection?> = _collection

    @OptIn(ExperimentalCoroutinesApi::class)
    val collectionPhotos: Flow<PagingData<Photo>> = collection
        .filterNotNull()
        .flatMapLatest { collectionsUseCase.getCollectionPhotos(it.id) }
        .cachedIn(viewModelScope)

    fun loadCollection(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val collection = collectionsUseCase.getCollection(id)
            _collection.emit(collection)
        }
    }
}