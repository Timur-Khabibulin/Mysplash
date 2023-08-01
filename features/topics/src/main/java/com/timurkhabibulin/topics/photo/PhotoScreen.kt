package com.timurkhabibulin.topics.photo

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.core.R.drawable
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.ui.util.UserView


@Composable
internal fun PhotoScreen(
    photoID: String,
    onBackPressed: () -> Unit,
    onNavigateToUser: (User) -> Unit,
    photoScreenViewModel: PhotoScreenViewModel = hiltViewModel()
) {
    photoScreenViewModel.loadPhoto(photoID)
    val photo: Photo by photoScreenViewModel.photo.collectAsState(initial = Photo.Default)
    val context = LocalContext.current

    PhotoInfoScreen(
        photo = photo,
        onBackPressed = onBackPressed,
        onDownloadPhoto = { ph ->
            photoScreenViewModel.downloadPhoto(ph,
                onStartDownload = {
                    Toast.makeText(context, "Download has begun", Toast.LENGTH_SHORT).show()
                },
                onDownloadComplete = {
                    Toast.makeText(context, "Download is complete", Toast.LENGTH_SHORT).show()
                }
            )
        },
        onUserClick = { user -> onNavigateToUser(user) }
    )

}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
internal fun PhotoInfoScreenPreview() {
    MysplashTheme {
        PhotoInfoScreen(Photo.Default, {}, {}, {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PhotoInfoScreen(
    photo: Photo,
    onBackPressed: () -> Unit,
    onDownloadPhoto: (Photo) -> Unit,
    onUserClick: (User) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    Box(contentAlignment = Alignment.BottomEnd) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 120.dp,
            sheetContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            topBar = { TopBar(onBackPressed) },
            sheetContent = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 20.dp, bottom = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.Start,
                ) {
                    UserView(
                        modifier = Modifier.fillMaxWidth(),
                        photo = photo
                    ) { user ->
                        onUserClick(
                            user
                        )
                    }

                    photo.description?.let {
                        Text(
                            text = it,
                            Modifier.width(280.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )
                    }

                    Exif(photo)
                }
            }
        ) { innerPadding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center
            ) {
                Photo(photo)
            }
        }
        FABs(
            onLikePhoto = {},
            onAddPhoto = {},
            onDownloadPhoto = { onDownloadPhoto(photo) }
        )
    }
}

@Composable
internal fun TopBar(onBackPressed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back icon",
            Modifier.clickable { onBackPressed() }
        )
        Icon(
            painter = painterResource(drawable.arrow_square_up),
            contentDescription = "Open in browser icon"
        )
    }
}

@Composable
internal fun Photo(photo: Photo) {
    val ratio = photo.width.toFloat() / photo.height

    var isChanged by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
        isChanged = true
    }.apply {
        if (isChanged && !isTransformInProgress) {
            scale = 1f
            offset = Offset.Zero
            isChanged = false
        }
    }

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
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
            .transformable(state)
    )
}

@Composable
internal fun Exif(photo: Photo) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.Start)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            ExifParameter("Camera", photo.exif?.model ?: "Unknown")
            ExifParameter("Focal length", photo.exif?.focal_length ?: "Unknown")
            ExifParameter("ISO", photo.exif?.iso.toString())
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            ExifParameter("Aperture", photo.exif?.aperture ?: "Unknown")
            ExifParameter("Exposure", photo.exif?.exposure_time ?: "Unknown")
        }
    }
}

@Composable
internal fun FABs(
    onLikePhoto: () -> Unit,
    onAddPhoto: () -> Unit,
    onDownloadPhoto: () -> Unit
) {
    Column(
        modifier = Modifier.padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
    ) {
        FloatingActionButton(
            shape = CircleShape,
            onClick = { onLikePhoto() }
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "like icon"
            )
        }

        FloatingActionButton(
            shape = CircleShape,
            onClick = { onAddPhoto() }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add icon"
            )
        }

        FloatingActionButton(
            shape = CircleShape,
            onClick = { onDownloadPhoto() },
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ) {
            Icon(
                painter = painterResource(drawable.download_02),
                contentDescription = "download icon"
            )
        }
    }

}

@Composable
internal fun ExifParameter(name: String, value: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = name, style = MaterialTheme.typography.titleSmall)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}