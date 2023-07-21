package com.timurkhabibulin.mysplash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.core.FeatureApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    val features: Set<@JvmSuppressWildcards FeatureApi>
) : ViewModel() {

    private val _isFullScreen = MutableStateFlow(false)
    val isFullScreen: StateFlow<Boolean> = _isFullScreen

    fun changeScreenMode(isFullScreen: Boolean) {
        viewModelScope.launch {
            _isFullScreen.emit(isFullScreen)
        }
    }
}