package com.timurkhabibulin.search

import com.timurkhabibulin.search.filter.FilterUIState

data class SearchScreenFiltersState(
    val query: String = "",
    val category: SearchCategory = SearchCategory.PHOTOS,
    val filters: FilterUIState = FilterUIState()
)
