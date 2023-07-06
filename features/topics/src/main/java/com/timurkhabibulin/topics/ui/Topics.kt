package com.timurkhabibulin.topics.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.topics.TopicsScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun TabScreen(
    topicsScreenViewModel: TopicsScreenViewModel = hiltViewModel()
) {

    val topics: List<Topic> by topicsScreenViewModel.topics.collectAsStateWithLifecycle(initialValue = listOf())
    val pagerState = rememberPagerState(pageCount = { topics.size })

    Column(Modifier.fillMaxSize()) {
        Tabs(tabs = topics, pagerState = pagerState)
        TabsContent(topics = topics, pagerState = pagerState)
    }
    /*    Surface {
            TabsContent(topic = topics, pagerState = pagerState)
            Tabs(tabs = topics, pagerState = pagerState)
        }*/
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(tabs: List<Topic>, pagerState: PagerState) {
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
                text = { Text(topic.title) },
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
fun TabsContent(
    topics: List<Topic>,
    pagerState: PagerState,
    topicsScreenViewModel: TopicsScreenViewModel = hiltViewModel()
) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        pageSpacing = 20.dp
    ) { page ->

        val photos =
            if (page == 0) topicsScreenViewModel.getPhotos()
            else topicsScreenViewModel.getPhotosFromTopic(topics[page - 1].id)

        PhotosList(photos) {
            if (page != 0) TopicPhotoPreview(topics[page - 1])
        }
    }
}