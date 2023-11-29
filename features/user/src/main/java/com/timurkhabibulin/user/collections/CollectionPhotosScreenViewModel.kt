package com.timurkhabibulin.user.collections

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.timurkhabibulin.core.BaseViewModel
import com.timurkhabibulin.core.asSuccessfulCompletion
import com.timurkhabibulin.core.isSuccessfulCompletion
import com.timurkhabibulin.domain.collections.CollectionsUseCase
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
internal class CollectionPhotosScreenViewModel @Inject constructor(
    private val collectionsUseCase: CollectionsUseCase
) : BaseViewModel<Collection>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val collectionPhotos: Flow<PagingData<Photo>> = state
        .filter { it.isSuccessfulCompletion() }
        .flatMapLatest {
            collectionsUseCase.getCollectionPhotos(it.asSuccessfulCompletion().value.id)
        }
        .cachedIn(viewModelScope)

    fun loadCollection(id: String) {
        loadData {
            collectionsUseCase.getCollection(id)
        }
    }
}
