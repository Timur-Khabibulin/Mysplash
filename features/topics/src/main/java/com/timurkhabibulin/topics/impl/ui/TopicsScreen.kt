package com.timurkhabibulin.topics.impl.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.entities.User
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TopicsScreen(
    onNavigateToUser: (User) -> Unit,
    onNavigateToPhoto: (Photo) -> Unit,
    topicsScreenViewModel: TopicsScreenViewModel = hiltViewModel()
) {

    val topics: List<Topic> by topicsScreenViewModel.topics.collectAsState()//.collectAsStateWithLifecycle(initialValue = listOf())
    val pagerState = rememberPagerState(pageCount = { topics.size })

    Column(Modifier.fillMaxSize()) {
        Tabs(tabs = topics, pagerState = pagerState)
        TabsContent(
            topics = topics,
            pagerState = pagerState,
            onNavigateToPhoto = { photo -> onNavigateToPhoto(photo) },
            onNavigateToUser = { user -> onNavigateToUser(user) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Tabs(tabs: List<Topic>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage
    ) {
        Tab(icon = {},
            text = { Text("Home") },
            selected = pagerState.currentPage == 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        )

        tabs.forEachIndexed { index, topic ->
            Tab(
                icon = { },
                text = { Text(topic.title, style = MaterialTheme.typography.titleMedium) },
                selected = pagerState.currentPage == index + 1,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index + 1)
                    }
                },
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
        pageSpacing = 20.dp
    ) { page ->

        val photos =
            if (page == 0) topicsScreenViewModel.photos
            else topicsScreenViewModel.photosByTopic[topics[page - 1].id]
                ?: flow { PagingData.empty<Photo>() }

        PagingPhotosList(
            photos,
            onUserClick = { user -> onNavigateToUser(user) },
            onPhotoClick = { photo -> onNavigateToPhoto(photo) },
            header = {
                if (page != 0) AboutTopic(topics[page - 1])
            })
    }
}