package com.timurkhabibulin.topics.impl.ui

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PagingPhotosList(
    items: Flow<PagingData<Photo>>,
    header: (@Composable () -> Unit)? = null,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit
) {
    val photos = items.collectAsLazyPagingItems()

    val refreshing = photos.loadState.refresh == LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { photos.refresh() })

    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (photos.loadState.refresh is LoadState.Error)
            OnLoadingError((photos.loadState.refresh as LoadState.Error).error.message!!)
        else PhotosList(
            photos,
            onPhotoClick = { ph -> onPhotoClick(ph) },
            onUserClick = { us -> onUserClick(us) },
            header
        )

        PullRefreshIndicator(
            modifier = Modifier.align(alignment = Alignment.TopCenter),
            refreshing = refreshing,
            state = pullRefreshState,
        )
    }

}

@Composable
internal fun OnLoadingError(errorMessage: String) {
    Text(
        text = errorMessage,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(20.dp)
    )
}

@Composable
@Preview
internal fun PhotosListPreview() {
    PhotosList(flow {
        emit(PagingData.from(listOf(Photo.Default, Photo.Default)))
    }.collectAsLazyPagingItems(), {}, {})
}

@Composable
internal fun PhotosList(
    photos: LazyPagingItems<Photo>,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    header: (@Composable () -> Unit)? = null
) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        header?.let { item { it() } }
        items(photos.itemCount) { index ->
            val photo = photos[index] ?: return@items
            PhotoCard(
                photo,
                onPhotoClick = { ph -> onPhotoClick(ph) },
                onUserClick = { us -> onUserClick(us) })
        }
    }
}


@Composable
@Preview
internal fun PhotoCardPrevew() {
    PhotoCard(Photo.Default, {}, {})
}

@Composable
internal fun PhotoCard(
    photo: Photo,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 20.dp)
    ) {
        User(photo) { us -> onUserClick(us) }
        PhotoView(photo) { ph -> onPhotoClick(ph) }
    }
}

@Composable
internal fun User(photo: Photo, onUserClick: (User) -> Unit) {
    Row(
        Modifier.clickable { onUserClick(photo.user) },
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.user.profile_image.large)
                .diskCachePolicy(CachePolicy.ENABLED)
                .placeholder(ColorDrawable(photo.color!!.toColorInt()))
                .crossfade(250)
                .build(),
            contentDescription = "Profile image",
            modifier = Modifier
                .width(45.dp)
                .height(45.dp)
                .clip(CircleShape)
        )
        Column {
            Text(
                text = photo.user.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
internal fun PhotoView(photo: Photo, onPhotoClick: (Photo) -> Unit) {
    val ratio = photo.width.toFloat() / photo.height
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photo.urls.regular)
            .diskCachePolicy(CachePolicy.ENABLED)
            .placeholder(ColorDrawable(photo.color!!.toColorInt()))
            .crossfade(250)
            .build(),
        contentDescription = "Image",
        modifier = Modifier
            .aspectRatio(if (ratio > 0) ratio else 1 / ratio)
            .clip(RoundedCornerShape(size = 10.dp))
            .clickable(onClick = { onPhotoClick(photo) })
    )
}