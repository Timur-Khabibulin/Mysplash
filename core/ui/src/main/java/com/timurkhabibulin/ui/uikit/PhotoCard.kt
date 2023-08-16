package com.timurkhabibulin.ui.uikit

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.theme.MysplashTheme

@Composable
@Preview
internal fun PhotoCardPrevew() {
    MysplashTheme {
        PhotoCard(
            photo = Photo.Default,
            onPhotoClick = {},
            onUserClick = {}
        )
    }
}

@Composable
fun PhotoCard(
    photo: Photo,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit
) {
    /*    PhotoCardVertical(
            photo = photo,
            onPhotoClick = onPhotoClick,
            onUserClick = onUserClick
        )*/
    PhotoCardHorizontal(
        photo = photo,
        onPhotoClick = onPhotoClick,
        onUserClick = onUserClick
    )

}

@Composable
private fun PhotoCardVertical(
    photo: Photo,
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        UserViewVertical(photo = photo, onUserClick = onUserClick)
        PhotoView(photo) { ph -> onPhotoClick(ph) }
    }
}

@Composable
private fun PhotoCardHorizontal(
    photo: Photo,
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        UserViewHorizontal(
            modifier = Modifier.fillMaxWidth(),
            photo = photo,
            onUserClick = onUserClick
        )
        PhotoView(photo) { ph -> onPhotoClick(ph) }
    }
}

@Composable
fun PhotoView(
    photo: Photo,
    modifier: Modifier = Modifier,
    keepPhotoAspectRatio: Boolean = true,
    onPhotoClick: (Photo) -> Unit,
) {
    val ratio = photo.width.toFloat() / photo.height
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photo.urls.regular)
            .diskCachePolicy(CachePolicy.ENABLED)
            .placeholder(ColorDrawable(photo.color?.toColorInt() ?: Color.Black.toArgb()))
            .crossfade(250)
            .build(),
        contentDescription = "Image",
        modifier = modifier
            .then(
                if (keepPhotoAspectRatio)
                    Modifier.aspectRatio(if (ratio > 0) ratio else 1 / ratio)
                else Modifier
            )
            .clip(RoundedCornerShape(size = 10.dp))
            .clickable(onClick = { onPhotoClick(photo) }),
        contentScale = ContentScale.Crop
    )
}
