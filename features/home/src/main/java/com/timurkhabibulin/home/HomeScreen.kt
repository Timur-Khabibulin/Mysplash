package com.timurkhabibulin.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.ui.uikit.CollapsibleLayout
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalStaggeredGrid
import com.timurkhabibulin.ui.uikit.PhotoCard
import com.timurkhabibulin.ui.uikit.TopicCard
import com.timurkhabibulin.ui.uikit.vertical
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
internal fun HomeScreen(
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    onTopicClick: (Topic) -> Unit,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val exploreDefaultHeight = 139.dp
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val canCollapse = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(lazyStaggeredGridState) {
        snapshotFlow { lazyStaggeredGridState.firstVisibleItemIndex }.collect {
            canCollapse.value = lazyStaggeredGridState.firstVisibleItemIndex == 0
        }
    }

    CollapsibleLayout(
        Modifier.padding(start = 10.dp, top = 20.dp, end = 10.dp),
        collapsingHeader = {
            Explore(
                it,
                topics = homeScreenViewModel.topics,
                onTopicClick = onTopicClick,
            )
        },
        content = {
            EditorialFeed(
                it.padding(top = 20.dp),
                photos = homeScreenViewModel.photos,
                onPhotoClick = onPhotoClick,
                onUserClick = onUserClick,
                lazyStaggeredGridState = lazyStaggeredGridState
            )
        },
        collapsingHeaderDefaultHeight = exploreDefaultHeight,
        canCollapse = canCollapse
    )
}

@Preview
@Composable
fun ExplorePreview() {
    MysplashTheme {
        Explore(topics = remember { mutableStateOf(listOf(Topic.Default, Topic.Default)) }) {}
    }
}

@Composable
fun Explore(
    modifier: Modifier = Modifier, topics: State<List<Topic>>, onTopicClick: (Topic) -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            modifier = Modifier
                .vertical()
                .rotate(-90f),
            text = stringResource(R.string.explore),
            style = MaterialTheme.typography.headlineSmall
        )

        LazyRow(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
        ) {
            items(topics.value.size) { index ->
                TopicCard(topics.value[index], onTopicClick)
            }
        }
    }
}

@Preview
@Composable
internal fun EditorialFeedPreview() {
    MysplashTheme {
        EditorialFeed(Modifier, photos = flow {
            emit(PagingData.from(listOf(Photo.Default, Photo.Default)))
        }, onPhotoClick = {}, onUserClick = {}, rememberLazyStaggeredGridState())
    }
}

@Composable
internal fun EditorialFeed(
    modifier: Modifier = Modifier,
    photos: Flow<PagingData<Photo>>,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    lazyStaggeredGridState: LazyStaggeredGridState
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.editorial),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Right
        )
        PagingPullRefreshVerticalStaggeredGrid(
            items = photos,
            itemCard = { photo ->
                PhotoCard(
                    photo = photo,
                    onPhotoClick = onPhotoClick,
                    onUserClick = onUserClick
                )
            },
            columns = StaggeredGridCells.Adaptive(300.dp),
            verticalItemSpacing = 20.dp,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            state = lazyStaggeredGridState
        )
    }
}
