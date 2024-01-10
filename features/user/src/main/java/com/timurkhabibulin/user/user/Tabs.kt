package com.timurkhabibulin.user.user

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.timurkhabibulin.core.asSuccessfulCompletion
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.ui.uikit.CollectionCard
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalColumn
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalStaggeredGrid
import com.timurkhabibulin.ui.uikit.PhotoView
import com.timurkhabibulin.user.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Tabs(
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit,
    onCollectionClick: (String) -> Unit
) {
    val tabs = listOf(
        stringResource(R.string.photos),
        stringResource(R.string.likes),
        stringResource(R.string.collections)
    )
    val pagerState = rememberPagerState(pageCount = { tabs.count() })

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CustomTabRow(
            pagerState.currentPage,
            tabs
        ) { index -> pagerState.animateScrollToPage(index) }
        UserTabsContent(
            pagerState = pagerState,
            onPhotoClick = { ph -> onPhotoClick(ph) },
            onCollectionClick = onCollectionClick
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun CustomTabRowPreview() {
    MysplashTheme {
        CustomTabRow(
            selectedIndex = 0,
            tabs = listOf(
                stringResource(R.string.photos),
                stringResource(R.string.likes),
                stringResource(R.string.collections)
            ),
            onClick = {}
        )
    }
}

@Composable
internal fun CustomTabRow(
    selectedIndex: Int,
    tabs: List<String>,
    onClick: suspend (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    TabRow(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(size = 10.dp))
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.secondaryContainer),
                RoundedCornerShape(size = 10.dp)
            ),
        containerColor = MaterialTheme.colorScheme.surface,
        selectedTabIndex = selectedIndex,
        indicator = { tabPositions ->
            UserTabIndicator(tabPositions[selectedIndex])
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tab ->
            UserCustomTab(
                text = tab,
                selected = selectedIndex == index,
                onClick = {
                    scope.launch { onClick(index) }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun UserTabsContent(
    pagerState: PagerState,
    onPhotoClick: (Photo) -> Unit,
    onCollectionClick: (String) -> Unit,
    userScreenViewModel: UserScreenViewModel = hiltViewModel()
) {
    val userPhotos = remember { userScreenViewModel.userPhotos }
    val userLikedPhotos = remember { userScreenViewModel.userLikedPhotos }
    val userCollections = remember { userScreenViewModel.userCollections }
    val user = userScreenViewModel.state.value.asSuccessfulCompletion().value

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp),
        pageSpacing = 20.dp
    ) { page ->

        when (page) {
            0 -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "${user.total_photos} ${stringResource(id = R.string.photos)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    PagingPullRefreshVerticalStaggeredGrid(
                        items = userPhotos,
                        itemCard = { photo ->
                            PhotoView(
                                photo = photo,
                                onPhotoClick = { onPhotoClick(photo) }
                            )
                        },
                        columns = StaggeredGridCells.Adaptive(160.dp),
                        verticalItemSpacing = 15.dp,
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                    )
                }
            }

            1 -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "${user.total_likes} ${stringResource(id = R.string.likes)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    PagingPullRefreshVerticalStaggeredGrid(
                        items = userLikedPhotos,
                        itemCard = { photo ->
                            PhotoView(
                                photo = photo,
                                onPhotoClick = { onPhotoClick(photo) }
                            )
                        },
                        columns = StaggeredGridCells.Adaptive(160.dp),
                        verticalItemSpacing = 15.dp,
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                    )
                }
            }

            2 -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "${user.total_collection} ${stringResource(id = R.string.collections)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    PagingPullRefreshVerticalColumn(
                        items = userCollections,
                        itemCard = { collection ->
                            CollectionCard(
                                collection = collection,
                                onClick = { onCollectionClick(it.id) }
                            )
                        },
                        space = 20.dp
                    )
                }
            }
        }

    }
}

@Composable
internal fun UserTabIndicator(tabPosition: TabPosition) {
    Box(
        modifier = Modifier
            .tabIndicatorOffset(tabPosition)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                RoundedCornerShape(10.dp)
            )
            .zIndex(-1f),
    )
}

@Composable
internal fun UserCustomTab(
    text: String,
    selected: Boolean,
    onClick: suspend () -> Unit
) {
    val scope = rememberCoroutineScope()
    Tab(
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
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