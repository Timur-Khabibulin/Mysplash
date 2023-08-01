package com.timurkhabibulin.user.user

import android.content.res.Configuration
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.core.R
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User


@Composable
internal fun UserScreen(
    username: String,
    onPhotoClick: (Photo) -> Unit,
    onBackPressed: () -> Unit,
    onCollectionClick: (String) -> Unit,
    userScreenViewModel: UserScreenViewModel = hiltViewModel()
) {
    userScreenViewModel.loadUser(username)
    val user by userScreenViewModel.user.collectAsState()

    Scaffold(topBar = { TopBar(onBackPressed = onBackPressed) }) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            UserInfo(user = user)
            Tabs(
                onPhotoClick = onPhotoClick,
                onCollectionClick = onCollectionClick
            )
        }
    }
}

@Composable
internal fun TopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackPressed: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back icon",
            Modifier.clickable { onBackPressed() }
        )
        title?.let {
            Text(text = it, style = MaterialTheme.typography.titleLarge)
        }
        Icon(
            painter = painterResource(R.drawable.arrow_square_up),
            contentDescription = "Open in browser icon"
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun UserInfoPreview() {
    com.timurkhabibulin.ui.theme.MysplashTheme {
        Surface {
            UserInfo(User.DefaultUser)
        }
    }
}

@Composable
internal fun UserInfo(user: User, modifier: Modifier = Modifier) {
    Column(
        modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        UserBasicInfo(user = user)
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            user.location?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall)
            }
            user.bio?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
        /*        user.badge?.let {
                    UserBadge(it)
                }*/
    }
}

@Composable
internal fun UserBasicInfo(user: User) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.profile_image.large)
                .diskCachePolicy(CachePolicy.ENABLED)
                .placeholder(ColorDrawable(Color.Black.toArgb()))
                .crossfade(250)
                .build(),
            contentDescription = "Profile image",
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .clip(CircleShape)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(text = user.name, style = MaterialTheme.typography.titleLarge)

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Param("Follow", user.followers_count.toString())
                Param("Following", user.following_count.toString())
            }
        }
    }
}

/*@Composable
internal fun UserBadge(badge: Badge) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp*//*, vertical = 5.dp*//*)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = badge.title, style = MaterialTheme.typography.bodyMedium)
            Icon(
                painterResource(long_arrow),
                contentDescription = ""
            )
        }
    }
}*/

@Composable
internal fun Param(name: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(text = value, style = MaterialTheme.typography.labelMedium)
    }
}