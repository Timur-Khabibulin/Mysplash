package com.timurkhabibulin.user.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalStaggeredGrid
import com.timurkhabibulin.ui.uikit.PhotoView
import com.timurkhabibulin.user.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
internal fun FavoritesScreen(
    onPhotoClick: (Photo) -> Unit,
    onMenuClick: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    FavoritesScreen(
        onPhotoClick = onPhotoClick,
        items = viewModel.state,
        onMenuClick = onMenuClick
    )
}

@Preview
@Composable
fun FavoritesScreenPreview() {
    MysplashTheme {
        FavoritesScreen(
            onPhotoClick = {},
            items = flow {
                PagingData.from(listOf(Photo.Default, Photo.Default, Photo.Default))
            },
            onMenuClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesScreen(
    onPhotoClick: (Photo) -> Unit,
    items: Flow<PagingData<Photo>>,
    onMenuClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.favorite),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                scrollBehavior = scrollBehavior,
                actions = {
                    Icon(
                        modifier = Modifier.clickable { onMenuClick() },
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }
            )
        }
    ) { paddingValues ->
        PagingPullRefreshVerticalStaggeredGrid(
            modifier = Modifier.padding(paddingValues),
            items = items,
            itemCard = { photo ->
                PhotoView(
                    photo = photo,
                    onPhotoClick = onPhotoClick
                )
            },
            columns = StaggeredGridCells.Adaptive(160.dp),
            verticalItemSpacing = 15.dp,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            onEmpty = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_favorite_photos),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        )
    }
}