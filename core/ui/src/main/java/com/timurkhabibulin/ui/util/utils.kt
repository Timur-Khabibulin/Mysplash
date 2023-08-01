package com.timurkhabibulin.ui.util

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.domain.entities.Collection
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.theme.MysplashTheme

@Preview
@Composable
internal fun CollectionCardPreview() {
    CollectionCard(collection = Collection.Default, onClick = {})
}

@Composable
fun CollectionCard(
    modifier: Modifier = Modifier,
    collection: Collection,
    onClick: (Collection) -> Unit
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick(collection) },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(collection.cover_photo!!.urls.small)
                .diskCachePolicy(CachePolicy.ENABLED)
                .placeholder(ColorDrawable(collection.cover_photo!!.color!!.toColorInt()))
                .crossfade(250)
                .build(),
            contentDescription = "Image",
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(size = 10.dp)),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(Color(0xBF000000), BlendMode.Darken)
        )

        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = collection.title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${collection.total_photos} photos",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
@Preview
internal fun PhotoCardPrevew() {
    MysplashTheme {
        PhotoCard(
            photo = Photo.Default,
            onPhotoClick = {},
            userView = {
                UserView(
                    photo = it,
                    onUserClick = {}
                )
            }
        )
    }
}

@Composable
fun PhotoCard(
    photo: Photo,
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit,
    userView: (@Composable (Photo) -> Unit)? = null
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        userView?.invoke(photo)
        PhotoView(photo) { ph -> onPhotoClick(ph) }
    }
}

@Composable
fun PhotoView(
    photo: Photo,
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit
) {
    val ratio = photo.width.toFloat() / photo.height
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photo.urls.regular)
            .diskCachePolicy(CachePolicy.ENABLED)
            .placeholder(ColorDrawable(photo.color!!.toColorInt()))
            .crossfade(250)
            .build(),
        contentDescription = "Image",
        modifier = modifier
            .aspectRatio(if (ratio > 0) ratio else 1 / ratio)
            .clip(RoundedCornerShape(size = 10.dp))
            .clickable(onClick = { onPhotoClick(photo) })
    )
}

@Composable
fun UserView(
    modifier: Modifier = Modifier,
    photo: Photo,
    onUserClick: (User) -> Unit
) {
    Row(
        modifier.clickable { onUserClick(photo.user) },
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
