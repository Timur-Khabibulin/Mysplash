package com.timurkhabibulin.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.result.asFailure
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ItemsPagingSource<T>(
    private val startingPageIndex: Int = 1,
    private val dispatcher: CoroutineDispatcher,
    private val getItems: suspend (Int) -> Result<List<T>>,
) : PagingSource<Int, T>() where T : Any {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(-1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> =
        withContext(dispatcher) {
            val page = params.key ?: startingPageIndex
            val result = getItems(page)

            if (result.isSuccess()) LoadResult.Page(
                data = result.asSuccess().value,
                prevKey = if (page == startingPageIndex) null else page - 1,
                nextKey = if (result.asSuccess().value.isEmpty()) null else page + 1
            ) else {
                LoadResult.Error(result.asFailure().error!!)
            }
        }
}