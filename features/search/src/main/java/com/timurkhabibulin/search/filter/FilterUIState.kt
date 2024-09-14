package com.timurkhabibulin.search.filter

import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation

data class FilterUIState(
    val orientation: Orientation? = null,
    val color: Color? = null
)
