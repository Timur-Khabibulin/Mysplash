package com.timurkhabibulin.ui.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow

@Composable
fun <T : Any> PagingPullRefreshVerticalStaggeredGrid(
    modifier: Modifier = Modifier,
    items: Flow<PagingData<T>>,
    itemCard: @Composable (T) -> Unit,
    columns: StaggeredGridCells,
    verticalItemSpacing: Dp = 0.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(0.dp),
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    onEmpty: (@Composable () -> Unit)? = null
) {
    PagingPullRefresh(
        modifier,
        items = items,
        content = {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize(),
                columns = columns,
                verticalItemSpacing = verticalItemSpacing,
                horizontalArrangement = horizontalArrangement,
                content = {
                    items(it.itemCount) { index ->
                        val item = it[index] ?: return@items
                        itemCard(item)
                    }
                },
                state = state
            )
        },
        onEmpty = onEmpty
    )
}

@Composable
fun <T : Any> PagingPullRefreshVerticalColumn(
    modifier: Modifier = Modifier,
    items: Flow<PagingData<T>>,
    itemCard: @Composable (T) -> Unit,
    space: Dp,
    header: (@Composable () -> Unit)? = null,
    onEmpty: (@Composable () -> Unit)? = null
) {
    PagingPullRefresh(
        modifier,
        items = items,
        content = { lazyPagingItems ->
            LazyColumn(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(space),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                header?.let {
                    item {
                        it()
                    }
                }

                items(lazyPagingItems.itemCount) { index ->
                    val item = lazyPagingItems[index] ?: return@items
                    itemCard(item)
                }
            }
        },
        onEmpty = onEmpty
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Any> PagingPullRefresh(
    modifier: Modifier = Modifier,
    items: Flow<PagingData<T>>,
    content: @Composable (LazyPagingItems<T>) -> Unit,
    onRefresh: (() -> Unit)? = null,
    onEmpty: (@Composable () -> Unit)? = null
) {
    val pagingItems = items.collectAsLazyPagingItems()

    val refreshing = pagingItems.loadState.refresh is LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            pagingItems.refresh()
            onRefresh?.invoke()
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
        } else if (onEmpty != null &&
            pagingItems.loadState.prepend.endOfPaginationReached &&
            pagingItems.itemCount == 0
        ) {
            onEmpty()
        } else content(pagingItems)

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