package com.timurkhabibulin.domain

sealed class LoadState<T> {

    class Loading<T> : LoadState<T>()

    sealed class Completed<T> : LoadState<T>() {

        class Success<T>(
            val value: T
        ) : LoadState<T>()

        class Failure<T>(val error: Throwable? = null) : LoadState<T>()
    }
}


fun <T> LoadState<T>.isLoading(): Boolean {
    return this is LoadState.Loading
}

fun <T> LoadState<T>.isSuccessfulCompletion(): Boolean {
    return this is LoadState.Completed.Success
}

fun <T> LoadState<T>.asSuccessfulCompletion(): LoadState.Completed.Success<T> {
    return this as LoadState.Completed.Success<T>
}

inline fun <T> LoadState<T>.onSuccess(action: (value: T) -> Unit): LoadState<T> {
    if (this.isSuccessfulCompletion()) action(this.asSuccessfulCompletion().value)
    return this
}

fun <T> LoadState<T>.isFailedCompletion(): Boolean {
    return this is LoadState.Completed.Failure
}

fun <T> LoadState<T>.asFailedCompletion(): LoadState.Completed.Failure<*> {
    return this as LoadState.Completed.Failure<*>
}

inline fun <T> LoadState<T>.onFailure(action: (error: Throwable?) -> Unit): LoadState<*> {
    if (this.isFailedCompletion()) action(this.asFailedCompletion().error)
    return this
}