package com.timurkhabibulin.core

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.domain.LoadState
import com.timurkhabibulin.domain.entities.Linkable
import com.timurkhabibulin.domain.isLoading
import com.timurkhabibulin.domain.onSuccess
import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.result.onFailure
import com.timurkhabibulin.domain.result.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T : Linkable> : ViewModel() {
    private val _state = MutableStateFlow<LoadState<T>>(LoadState.Loading())
    val state: StateFlow<LoadState<T>> = _state

    fun openDeepLink(context: Context) {
        state.value.onSuccess {
            it.links.html?.let { link ->
                val deepLinkIntent = Intent(
                    Intent.ACTION_VIEW,
                    link.toUri()
                )
                context.startActivity(deepLinkIntent)
            }
        }
    }

    protected fun loadData(getData: suspend () -> Result<T>) {
        viewModelScope.launch {
            if (_state.value.isLoading()) {
                getData().onSuccess {
                    _state.value = LoadState.Completed.Success(it)
                }.onFailure {
                    _state.value = LoadState.Completed.Failure(it)
                }
            }
        }
    }
}
