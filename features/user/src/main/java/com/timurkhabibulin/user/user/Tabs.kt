package com.timurkhabibulin.user.user

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.ui.util.CollectionCard
import com.timurkhabibulin.ui.util.PagingPullRefresh
import com.timurkhabibulin.ui.util.PhotoCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Tabs(
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit,
    onCollectionClick: (String) -> Unit
) {
    val tabs = listOf("Photos", "Likes", "Collections")
    val pagerState = rememberPagerState(pageCount = { tabs.count() })

    Column(
        modifier = modifier.fillMaxWidth(),
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
    com.timurkhabibulin.ui.theme.MysplashTheme {
        CustomTabRow(
            selectedIndex = 0,
            tabs = listOf("Photos", "Likes"),
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
            .padding(top = 10.dp, bottom = 10.dp)
            .clip(shape = RoundedCornerShape(size = 10.dp)),
        containerColor = MaterialTheme.colorScheme.secondary,
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

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
        pageSpacing = 20.dp
    ) { page ->
        when (page) {

            0 -> PagingPullRefresh(
                items = userPhotos,
            ) { photos ->
                PhotosGrid(photos = photos) { photo ->
                    PhotoCard(
                        photo = photo,
                        onPhotoClick = { onPhotoClick(photo) }
                    )
                }
            }

            1 -> PagingPullRefresh(items = userLikedPhotos) { photos ->
                PhotosGrid(photos = photos) { photo ->
                    PhotoCard(
                        photo = photo,
                        onPhotoClick = { onPhotoClick(photo) }
                    )
                }
            }

            2 -> PagingPullRefresh(items = userCollections) { collections ->
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
                ) {
                    items(collections.itemCount) { index ->
                        val collection = collections[index] ?: return@items
                        CollectionCard(
                            collection = collection,
                            onClick = { onCollectionClick(it.id) }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun PhotosGrid(
    photos: LazyPagingItems<Photo>,
    photoCard: @Composable (Photo) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 15.dp,
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        content = {
            items(photos.itemCount) { index ->
                val photo = photos[index] ?: return@items
                photoCard(photo)
            }
        }
    )
}

@Composable
internal fun UserTabIndicator(tabPosition: TabPosition) {
    Box(
        modifier = Modifier
            .tabIndicatorOffset(tabPosition)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primary,
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
                color = MaterialTheme.colorScheme.onPrimary
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