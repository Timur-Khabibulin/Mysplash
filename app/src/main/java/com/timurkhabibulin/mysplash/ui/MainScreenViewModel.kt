package com.timurkhabibulin.mysplash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    private val _isFullScreen = MutableStateFlow(false)
    val isFullScreen: StateFlow<Boolean> = _isFullScreen

    fun changeScreenMode(isFullScreen: Boolean) {
        viewModelScope.launch {
            _isFullScreen.emit(isFullScreen)
        }
    }
}