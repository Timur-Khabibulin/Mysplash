package com.timurkhabibulin.search

import androidx.lifecycle.ViewModel
import com.timurkhabibulin.domain.search.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {


}