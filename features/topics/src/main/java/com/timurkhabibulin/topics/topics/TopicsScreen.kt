package com.timurkhabibulin.topics.topics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.timurkhabibulin.domain.LoadState
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.photos.R
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalColumn
import com.timurkhabibulin.ui.uikit.PhotoCard
import com.timurkhabibulin.ui.uikit.Tab
import com.timurkhabibulin.ui.uikit.TabIndicator
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TopicsScreen(
    onNavigateToUser: (User) -> Unit,
    onNavigateToPhoto: (Photo) -> Unit,
    topicIdToOpen: String? = null,
    topicsScreenViewModel: TopicsScreenViewModel = hiltViewModel()
) {

    val state by topicsScreenViewModel.topics.collectAsState()

    when (val screenState = state) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
            }
        }

        is LoadState.Completed.Failure -> {
            Text(
                modifier = Modifier
                    .padding(20.dp),
                text = screenState.error?.message ?: "",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
        }

        is LoadState.Completed.Success -> {
            val pagerState = rememberPagerState(pageCount = { screenState.value.size })

            topicIdToOpen?.let { id ->
                val scope = rememberCoroutineScope()
                topicsScreenViewModel.getIndexOfTopic(id)?.let { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            }

            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        15.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.topics),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    ScrollableTab(tabs = screenState.value, pagerState = pagerState)
                }
                TabsContent(
                    topics = screenState.value,
                    pagerState = pagerState,
                    onNavigateToPhoto = { photo -> onNavigateToPhoto(photo) },
                    onNavigateToUser = { user -> onNavigateToUser(user) }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
internal fun ScrollableTabPreview() {
    val tabs = listOf(Topic.Default, Topic.Default)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    MysplashTheme {
        ScrollableTab(tabs, pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ScrollableTab(
    tabs: List<Topic>,
    pagerState: PagerState
) {
    ScrollableTabRow(
        modifier = Modifier
            .fillMaxWidth(),
        edgePadding = 10.dp,
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions -> TabIndicator(tabPositions[pagerState.currentPage]) },
        divider = {},
        containerColor = MaterialTheme.colorScheme.background
    ) {
        tabs.forEachIndexed { index, topic ->
            Tab(
                text = topic.title,
                selected = pagerState.currentPage == index,
                onClick = {
                    pagerState.animateScrollToPage(index)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TabsContent(
    topics: List<Topic>,
    pagerState: PagerState,
    onNavigateToUser: (User) -> Unit,
    onNavigateToPhoto: (Photo) -> Unit,
    topicsScreenViewModel: TopicsScreenViewModel = hiltViewModel()
) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        pageSpacing = 20.dp,
        contentPadding = PaddingValues(horizontal = 10.dp)
    ) { page ->

        val photos = topicsScreenViewModel.photosByTopic[topics[page].id]
            ?: flow { PagingData.empty<Photo>() }

        PagingPullRefreshVerticalColumn(
            items = photos,
            itemCard = { photo ->
                PhotoCard(
                    photo = photo,
                    onPhotoClick = { onNavigateToPhoto(photo) },
                    onUserClick = { user -> onNavigateToUser(user) }
                )
            },
            space = 20.dp,
            header = { AboutTopic(topics[page]) }
        )
    }
}
