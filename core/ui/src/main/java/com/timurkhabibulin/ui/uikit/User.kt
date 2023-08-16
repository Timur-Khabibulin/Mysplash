package com.timurkhabibulin.ui.uikit

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.theme.MysplashTheme

@Preview
@Composable
internal fun UserPreviewCardPreview() {
    UserPreviewCard(
        user = User.DefaultUser,
        onUserClick = {},
        onPhotoClick = {}
    )
}

@Composable
fun UserPreviewCard(
    modifier: Modifier = Modifier,
    user: User,
    onUserClick: (User) -> Unit,
    onPhotoClick: (Photo) -> Unit
) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable { onUserClick(user) },
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Avatar(user = user, size = 50.dp)
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(text = user.name, style = MaterialTheme.typography.titleSmall)
                Text(text = "@${user.username}", style = MaterialTheme.typography.labelMedium)
            }
        }
        Row(
            Modifier.padding(start = 60.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            user.photos?.forEachIndexed { index: Int, photo: Photo ->
                PhotoView(
                    modifier = Modifier
                        .width(100.dp)
                        .height(120.dp),
                    keepPhotoAspectRatio = false,
                    photo = photo,
                    onPhotoClick = onPhotoClick
                )
                if (index == 2) return
            }
        }
    }
}

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
        Avatar(user = photo.user, size = 45.dp)
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
        Avatar(user = photo.user, size = 45.dp)
    }
}

@Composable
private fun Avatar(
    modifier: Modifier = Modifier,
    size: Dp,
    user: User
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(user.profile_image.large)
            .diskCachePolicy(CachePolicy.ENABLED)
            .placeholder(
                ColorDrawable(Color.Black.toArgb())
            )
            .crossfade(250)
            .build(),
        contentDescription = "Profile image",
        modifier = Modifier
            .width(size)
            .height(size)
            .clip(CircleShape)
    )
}