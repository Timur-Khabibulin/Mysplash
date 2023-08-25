package com.timurkhabibulin.topics.photo

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.core.R.drawable
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.domain.result.Result
import com.timurkhabibulin.domain.result.asFailure
import com.timurkhabibulin.domain.result.asSuccess
import com.timurkhabibulin.domain.result.isSuccess
import com.timurkhabibulin.photos.R
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.ui.uikit.TopBar
import com.timurkhabibulin.ui.uikit.UserViewHorizontal


@Composable
internal fun PhotoScreen(
    photoID: String,
    onBackPressed: () -> Unit,
    onNavigateToUser: (User) -> Unit,
    photoScreenViewModel: PhotoScreenViewModel = hiltViewModel()
) {
    photoScreenViewModel.loadPhoto(photoID)
    val result: Result<Photo> by photoScreenViewModel.photo.collectAsStateWithLifecycle(
        Result.Failure.Error(
            Throwable()
        )
    )
    val context = LocalContext.current
    val startDownload = stringResource(id = R.string.download_start)
    val stopDownload = stringResource(id = R.string.download_stop)

    if (result.isSuccess()) {
        PhotoInfoScreen(
            photo = result.asSuccess().value,
            onBackPressed = onBackPressed,
            onDownloadPhoto = { ph ->
                photoScreenViewModel.downloadPhoto(
                    ph,
                    onStartDownload = {
                        Toast.makeText(
                            context, startDownload, Toast.LENGTH_SHORT
                        ).show()
                    },
                    onDownloadComplete = {
                        Toast.makeText(
                            context, stopDownload, Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                )
            },
            onUserClick = { user -> onNavigateToUser(user) }
        )
    } else {
        Text(
            text = "${stringResource(R.string.error_loading)}. ${result.asFailure().error?.message}",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(20.dp),
            textAlign = TextAlign.Center
        )
    }

    /*    PhotoInfoScreen(
            photo = photo,
            onBackPressed = onBackPressed,
            onDownloadPhoto = { ph ->
                photoScreenViewModel.downloadPhoto(
                    ph,
                    onStartDownload = {
                        Toast.makeText(context, "Download has begun", Toast.LENGTH_SHORT).show()
                    },
                    onDownloadComplete = {
                        Toast.makeText(context, "Download is complete", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onUserClick = { user -> onNavigateToUser(user) }
        )*/

    /*    if (photoID.isNotEmpty()) {
            ShowPhoto(
                photoID = photoID,
                onBackPressed = onBackPressed,
                onNavigateToUser = onNavigateToUser
            )
        } else {
            Text(
                text = "Error loading",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Center
            )
        }*/
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
            modifier = Modifier.offset(0.dp),
            scaffoldState = scaffoldState,
            sheetPeekHeight = 120.dp,
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopBar(
                    onBackPressed = onBackPressed,
                    onOpenInBrowser = {}
                )
            },
            sheetContent = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 25.dp, end = 20.dp, bottom = 25.dp),
                    verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.Start,
                ) {
                    UserViewHorizontal(
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
                            style = MaterialTheme.typography.bodyMedium,
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
            ExifParameter(
                stringResource(R.string.camera),
                photo.exif?.model ?: stringResource(R.string.unknown)
            )
            ExifParameter(
                stringResource(R.string.focal_length),
                photo.exif?.focal_length ?: stringResource(R.string.unknown)
            )
            ExifParameter(stringResource(R.string.iso), photo.exif?.iso.toString())
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            ExifParameter(
                stringResource(R.string.aperture),
                photo.exif?.aperture ?: stringResource(R.string.unknown)
            )
            ExifParameter(
                stringResource(R.string.exposure),
                photo.exif?.exposure_time ?: stringResource(R.string.unknown)
            )
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
            onClick = { onLikePhoto() },
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "like icon"
            )
        }

        FloatingActionButton(
            shape = CircleShape,
            onClick = { onAddPhoto() },
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add icon"
            )
        }

        FloatingActionButton(
            shape = CircleShape,
            onClick = { onDownloadPhoto() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface
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
        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}