package com.timurkhabibulin.user.collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.uikit.PagingPullRefreshVerticalStaggeredGrid
import com.timurkhabibulin.ui.uikit.PhotoCard
import com.timurkhabibulin.ui.uikit.TopBar

@Composable
internal fun CollectionPhotosScreen(
    collectionId: String,
    onBackPressed: () -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    collectionPhotosScreenViewModel: CollectionPhotosScreenViewModel = hiltViewModel()
) {
    collectionPhotosScreenViewModel.loadCollection(collectionId)

    val collection by collectionPhotosScreenViewModel.collection.collectAsState()
    val photos = collectionPhotosScreenViewModel.collectionPhotos

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = collection?.title ?: "",
                onBackPressed = onBackPressed,
                onOpenInBrowser = {}
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            collection?.let { collection ->
                CollectionInfo(collection)
            }
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
            )
        }
    }
}

@Composable
internal fun CollectionInfo(collection: Collection) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        collection.description?.let { description ->
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "${collection.total_photos} photos • ${collection.user!!.name}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}