package com.timurkhabibulin.core

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.domain.entities.Linkable
import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.result.asFailure
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T : Linkable> : ViewModel() {
    private val _state = MutableStateFlow<LoadState<T>>(LoadState.Loading())
    val state: StateFlow<LoadState<T>> = _state

    fun openDeepLink(context: Context) {
        with(state.value) {
            if (this.isSuccessfulCompletion()) {
                this.asSuccessfulCompletion().value.links.html?.let {
                    val deepLinkIntent = Intent(
                        Intent.ACTION_VIEW,
                        it.toUri()
                    )
                    context.startActivity(deepLinkIntent)
                }
            }
        }
    }

    protected fun loadData(getData: suspend () -> Result<T>) {
        viewModelScope.launch(Dispatchers.IO) {

            if (_state.value.isLoading()) {
                val result = getData()

                _state.emit(
                    if (result.isSuccess())
                        LoadState.Completed.Success(result.asSuccess().value)
                    else LoadState.Completed.Failure(result.asFailure().error)
                )
            }
        }
    }
}
