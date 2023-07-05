package com.timurkhabibulin.topics.ui

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.timurkhabibulin.domain.entities.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow


@Composable
fun PhotosList(
    items: Flow<PagingData<Photo>>,
    header: (@Composable () -> Unit)? = null
) {
    val photos = items.collectAsLazyPagingItems()
    val swipeRfreshState =
        rememberSwipeRefreshState(isRefreshing = photos.loadState.refresh == LoadState.Loading)

    SwipeRefresh(
        state = swipeRfreshState,
        onRefresh = { photos.refresh() },
        Modifier.fillMaxHeight()
    ) {
        if (photos.loadState.refresh is LoadState.Error)
            OnLoadingError((photos.loadState.refresh as LoadState.Error).error.message!!)
        else OnLoadingSuccess(photos, header)
    }

}

@Composable
fun OnLoadingError(errorMessage: String) {
    Text(
        text = errorMessage,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(20.dp)
    )
}

@Composable
fun OnLoadingSuccess(
    photos: LazyPagingItems<Photo>,
    header: (@Composable () -> Unit)? = null
) {
    LazyColumn {
        header?.let { item { it() } }
        items(photos.itemCount) { index ->
            val photo = photos[index] ?: return@items
            PhotoCard(photo)
        }
    }
}

@Composable
fun PhotoCard(photo: Photo) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 5.dp)
    ) {
        User(photo)
        PhotoView(photo = photo)
    }
}

@Composable
fun User(photo: Photo) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .interceptorDispatcher(Dispatchers.IO)
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
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun PhotoView(photo: Photo) {
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
    )
}