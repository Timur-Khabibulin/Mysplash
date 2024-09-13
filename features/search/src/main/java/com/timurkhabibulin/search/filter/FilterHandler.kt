package com.timurkhabibulin.search.filter

import com.timurkhabibulin.domain.entities.Color
import com.timurkhabibulin.domain.entities.Orientation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FilterHandler {
    private val _uiState = MutableStateFlow(FilterUIState())
    val uiState = _uiState.asStateFlow()

    fun updateOrientation(orientation: Orientation?) {
        _uiState.update {
            it.copy(orientation = orientation)
        }
    }

    fun updateColor(color: Color?) {
        _uiState.update {
            it.copy(color = color)
        }
    }
}
