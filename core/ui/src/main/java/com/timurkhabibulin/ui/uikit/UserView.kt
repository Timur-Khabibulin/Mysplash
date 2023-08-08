package com.timurkhabibulin.ui.uikit

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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

@Preview
@Composable
internal fun UserViewHorizontalPreview() {
    MysplashTheme {
        UserViewHorizontal(photo = Photo.Default, onUserClick = {})
    }
}

@Composable
fun UserViewHorizontal(
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
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview
@Composable
internal fun UserViewVerticalPreview() {
    MysplashTheme {
        UserViewVertical(photo = Photo.Default, onUserClick = {})
    }
}

@Composable
fun UserViewVertical(
    modifier: Modifier = Modifier,
    photo: Photo,
    onUserClick: (User) -> Unit
) {
    Column(
        modifier.clickable { onUserClick(photo.user) },
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .vertical()
                .rotate(-90f),
            text = photo.user.name,
            style = MaterialTheme.typography.titleSmall
        )
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
    }
}