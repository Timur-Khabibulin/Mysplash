package com.timurkhabibulin.user.collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.util.PagingPullRefresh
import com.timurkhabibulin.ui.util.PhotoCard
import com.timurkhabibulin.ui.util.UserView
import com.timurkhabibulin.user.user.TopBar

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
    val photos = remember {
        collectionPhotosScreenViewModel.collectionPhotos
    }

    Scaffold(
        topBar = {
            TopBar(
                title = collection?.title ?: "",
                onBackPressed = onBackPressed
            )
        }
    ) {
        PagingPullRefresh(
            Modifier
                .padding(it)
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
            items = photos
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                collection?.let {
                    item { CollectionInfo(it) }
                }

                items(it.itemCount) { index ->
                    val photo = it[index] ?: return@items

                    PhotoCard(
                        photo = photo,
                        userView = { UserView(photo = photo, onUserClick = onUserClick) },
                        onPhotoClick = onPhotoClick
                    )
                }
            }
        }
    }
}

@Composable
internal fun CollectionInfo(collection: Collection) {
    Column {
        collection.description?.let { description ->
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Text(
            text = "${collection.total_photos} photos • ${collection.user!!.name}",
            style = MaterialTheme.typography.labelMedium
        )
    }
}