package com.timurkhabibulin.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
@Composable
fun <T : Any> PagingPullRefresh(
    modifier: Modifier = Modifier,
    items: Flow<PagingData<T>>,
    itemsList: @Composable (LazyPagingItems<T>) -> Unit
) {
    val pagingItems = items.collectAsLazyPagingItems()

    val refreshing = pagingItems.loadState.refresh is LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            pagingItems.refresh()
        }
    )

    Box(
        modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (pagingItems.loadState.refresh is LoadState.Error) {
            val error = (pagingItems.loadState.refresh as LoadState.Error).error
            OnLoadingError(error.message ?: "")
        } else itemsList(pagingItems)

        PullRefreshIndicator(
            modifier = Modifier.align(alignment = Alignment.TopCenter),
            refreshing = refreshing,
            state = pullRefreshState,
        )
    }
}

@Composable
internal fun OnLoadingError(errorMessage: String) {
    Text(
        text = errorMessage,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(20.dp)
    )
}