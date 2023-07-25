package com.timurkhabibulin.topics.impl.ui.photo

import android.graphics.drawable.ColorDrawable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.topics.impl.ui.User


@Composable
internal fun PhotoScreen(
    photoID: String,
    onBackPressed: () -> Unit,
    photoScreenViewModel: PhotoScreenViewModel = hiltViewModel()
) {
    photoScreenViewModel.loadPhoto(photoID)
    val photo: Photo by photoScreenViewModel.photo.collectAsState(initial = Photo.Default)

    PhotoInfoScreen(
        photo = photo,
        onBackPressed = onBackPressed,
        onDownloadPhoto = { ph ->
            photoScreenViewModel.downloadPhoto(ph) /*{
                Toast.makeText(context, "Saved to Pictures", Toast.LENGTH_SHORT).show()
            }*/
        }
    )
}

@Composable
@Preview
internal fun PhotoInfoScreenPreview() {
    PhotoInfoScreen(Photo.Default, {}, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PhotoInfoScreen(
    photo: Photo,
    onBackPressed: () -> Unit,
    onDownloadPhoto: (Photo) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 120.dp,
        topBar = { TopBar(onBackPressed) },
        sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 25.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start,
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    User(photo) {}
                    FloatingActionButton(
                        shape = CircleShape,
                        onClick = { onDownloadPhoto(photo) }
                    ) {
                        Icon(
                            painter = painterResource(drawable.download_02),
                            contentDescription = "download icon"
                        )
                    }
                }

                photo.description?.let {
                    Text(
                        text = it,
                        Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
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
internal fun Exif(photo: Photo) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterHorizontally)
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
internal fun ExifParameter(name: String, value: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(text = name, style = MaterialTheme.typography.titleSmall)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}