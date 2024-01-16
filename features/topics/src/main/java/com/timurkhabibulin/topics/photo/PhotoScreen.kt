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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import com.timurkhabibulin.core.LoadState
import com.timurkhabibulin.core.R.drawable
import com.timurkhabibulin.core.analytics.AnalyticsAction
import com.timurkhabibulin.core.analytics.AnalyticsEvent
import com.timurkhabibulin.core.analytics.ContentType
import com.timurkhabibulin.core.asSuccessfulCompletion
import com.timurkhabibulin.core.isSuccessfulCompletion
import com.timurkhabibulin.core.utils.LocalAnalytics
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
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

    val state by photoScreenViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val startDownload = stringResource(id = R.string.download_start)
    val stopDownload = stringResource(id = R.string.download_stop)
    val analytics = LocalAnalytics.current

    PhotoInfoScreen(
        state = state,
        onBackPressed = onBackPressed,
        onDownloadPhoto = { ph ->
            analytics.logEvent(AnalyticsEvent(AnalyticsAction.DOWNLOAD, ContentType.BUTTON))
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
                    ).show()
                }
            )
        },
        onUserClick = { user -> onNavigateToUser(user) },
        onOpenInBrowser = {
            analytics.logEvent(
                AnalyticsEvent(
                    AnalyticsAction.OPEN_IN_BROWSER,
                    ContentType.BUTTON,
                    SCREEN_NAME
                )
            )
            photoScreenViewModel.openDeepLink(context)
        },
        onLikePhoto = {
            analytics.logEvent(AnalyticsEvent(AnalyticsAction.ADD_TO_FAVORITE, ContentType.BUTTON))
            photoScreenViewModel.onLikeClick(it)
        },
        likeState = photoScreenViewModel.isFavorite,
        onSetAsWallpaper = {
            analytics.logEvent(AnalyticsEvent(AnalyticsAction.SET_WALLPAPER, ContentType.BUTTON))
            photoScreenViewModel.setAsWallpaper()
        }
    )
}

@Composable
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
internal fun PhotoInfoScreenPreview() {
    MysplashTheme {
        val state = remember {
            mutableStateOf(false)
        }
        PhotoInfoScreen(LoadState.Completed.Success(Photo.Default), {}, {}, {}, {}, {}, state, {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PhotoInfoScreen(
    state: LoadState<Photo>,
    onBackPressed: () -> Unit,
    onDownloadPhoto: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    onOpenInBrowser: (String) -> Unit,
    onLikePhoto: (String) -> Unit,
    likeState: State<Boolean>,
    onSetAsWallpaper: () -> Unit
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
                    onOpenInBrowser = {
                        if (state.isSuccessfulCompletion()) {
                            state.asSuccessfulCompletion().value.links.html?.let {
                                onOpenInBrowser(it)
                            }
                        }
                    }
                )
            },
            sheetContent = {
                if (state.isSuccessfulCompletion()) {
                    SheetContent(
                        photo = state.asSuccessfulCompletion().value,
                        onUserClick = onUserClick
                    )
                }
            }
        ) { innerPadding ->

            when (state) {
                is LoadState.Completed.Failure -> {
                    Text(
                        text = "${stringResource(R.string.error_loading)}. ${state.error?.message}",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(20.dp),
                        textAlign = TextAlign.Center
                    )
                }

                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    }
                }

                is LoadState.Completed.Success -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding), contentAlignment = Alignment.Center
                    ) {
                        Photo(state.asSuccessfulCompletion().value)
                    }
                }
            }
        }
        if (state.isSuccessfulCompletion()) {
            FABs(
                likeState = likeState,
                onLikePhoto = { onLikePhoto(state.asSuccessfulCompletion().value.id) },
                onDownloadPhoto = { onDownloadPhoto(state.asSuccessfulCompletion().value) },
                onSetAsWallpaper = onSetAsWallpaper
            )
        }
    }
}

@Composable
fun SheetContent(
    photo: Photo,
    onUserClick: (User) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(start = 25.dp, end = 20.dp, bottom = 25.dp),
        verticalArrangement = Arrangement.spacedBy(
            25.dp,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.Start,
    ) {
        UserViewHorizontal(
            modifier = Modifier.fillMaxWidth(),
            photo = photo
        ) { user -> onUserClick(user) }

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
    likeState: State<Boolean>,
    onLikePhoto: () -> Unit,
    onDownloadPhoto: () -> Unit,
    onSetAsWallpaper: () -> Unit
) {
    Column(
        modifier = Modifier.padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top)
    ) {
        FloatingActionButton(
            shape = CircleShape,
            onClick = onSetAsWallpaper,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.surface
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wallpaper),
                contentDescription = "like icon"
            )
        }

        FloatingActionButton(
            shape = CircleShape,
            onClick = onLikePhoto,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = when (likeState.value) {
                    true -> Icons.Outlined.Favorite
                    false -> Icons.Outlined.FavoriteBorder
                },
                contentDescription = "like icon"
            )
        }

        FloatingActionButton(
            shape = CircleShape,
            onClick = onDownloadPhoto,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.primary
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
