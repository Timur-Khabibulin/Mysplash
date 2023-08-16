package com.timurkhabibulin.topics.topics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.entities.User
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

    val topics by topicsScreenViewModel.topics.collectAsState()

    if (topics.isNullOrEmpty()) {
        Text(
            modifier = Modifier
                .padding(20.dp),
            text = topicsScreenViewModel.errorMessage.value,
            style = MaterialTheme.typography.titleSmall
        )
    } else {
        val pagerState = rememberPagerState(pageCount = { topics!!.size })

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
                    text = "Topics",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                ScrollableTab(tabs = topics!!, pagerState = pagerState)
            }
            TabsContent(
                topics = topics!!,
                pagerState = pagerState,
                onNavigateToPhoto = { photo -> onNavigateToPhoto(photo) },
                onNavigateToUser = { user -> onNavigateToUser(user) }
            )
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
