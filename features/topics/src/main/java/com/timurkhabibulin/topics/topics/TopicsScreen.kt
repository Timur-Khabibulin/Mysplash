package com.timurkhabibulin.topics.topics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.util.PagingPullRefresh
import com.timurkhabibulin.ui.util.PhotoCard
import com.timurkhabibulin.ui.util.UserView
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TopicsScreen(
    onNavigateToUser: (User) -> Unit,
    onNavigateToPhoto: (Photo) -> Unit,
    topicsScreenViewModel: TopicsScreenViewModel = hiltViewModel()
) {

    val topics: List<Topic> by topicsScreenViewModel.topics.collectAsState()
    val pagerState = rememberPagerState(pageCount = { topics.size + 1 })

    Column(Modifier.fillMaxSize()) {
        ScrollableTab(tabs = topics, pagerState = pagerState)
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
@Preview
internal fun ScrollableTabPreview() {
    val tabs = listOf(Topic.Default, Topic.Default)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    com.timurkhabibulin.ui.theme.MysplashTheme {
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
        modifier = Modifier.padding(vertical = 5.dp),
        edgePadding = 10.dp,
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions -> TabIndicator(tabPositions[pagerState.currentPage]) },
        divider = {}
    ) {
        CustomTab(
            text = "Home",
            selected = pagerState.currentPage == 0,
            onClick = {
                pagerState.animateScrollToPage(0)
            }
        )

        tabs.forEachIndexed { index, topic ->
            CustomTab(
                text = topic.title,
                selected = pagerState.currentPage == index + 1,
                onClick = {
                    pagerState.animateScrollToPage(index + 1)
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
        pageSpacing = 20.dp
    ) { page ->

        val photos =
            if (page == 0) topicsScreenViewModel.photos
            else topicsScreenViewModel.photosByTopic[topics[page - 1].id]
                ?: flow { PagingData.empty<Photo>() }

        PagingPullRefresh(items = photos) { lazyPagingItems ->
            PhotosList(
                photos = lazyPagingItems,
                photoCard = { photo ->
                    PhotoCard(
                        photo = photo,
                        onPhotoClick = { onNavigateToPhoto(photo) },
                        userView = {
                            UserView(Modifier.fillMaxWidth(), photo) { user ->
                                onNavigateToUser(user)
                            }
                        }
                    )
                },
                header = {
                    if (page != 0)
                        AboutTopic(topics[page - 1])
                }
            )
        }
    }
}

@Composable
internal fun PhotosList(
    photos: LazyPagingItems<Photo>,
    photoCard: @Composable (Photo) -> Unit,
    header: (@Composable () -> Unit)? = null,
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        header?.let { item { it() } }
        items(photos.itemCount) { index ->
            val photo = photos[index] ?: return@items
            photoCard(photo)
        }
    }
}

@Composable
internal fun TabIndicator(tabPosition: TabPosition) {
    Box(
        modifier = Modifier
            .tabIndicatorOffset(tabPosition)
            .padding(horizontal = 5.dp)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(100))
            .zIndex(-1f),
    )
}

@Composable
internal fun CustomTab(
    text: String,
    selected: Boolean,
    onClick: suspend () -> Unit
) {
    val scope = rememberCoroutineScope()
    Tab(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer),
                RoundedCornerShape(100)
            ),
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        },
        selected = selected,
        onClick = {
            scope.launch {
                onClick()
            }
        }
    )
}