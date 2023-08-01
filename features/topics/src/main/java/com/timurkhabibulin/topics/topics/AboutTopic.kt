package com.timurkhabibulin.topics.topics

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.domain.entities.Topic

@Composable
@Preview
internal fun AboutTopicPreview() {
    AboutTopic(Topic.Default)
}

@Composable
internal fun AboutTopic(topic: Topic) {
    val photo = topic.cover_photo

    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp/*bottomEnd = 10.dp, bottomStart = 10.dp*/))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.urls.thumb)
                .diskCachePolicy(CachePolicy.ENABLED)
                .placeholder(ColorDrawable(photo.color!!.toColorInt()))
                .build(),
            contentDescription = "Topic preview image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(16 / 9f)
                .blur(50.dp)
        )

        Column(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = topic.title,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineSmall,

                color = Color.White
            )
            Text(
                text = topic.description,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                maxLines = 2
            )
        }
    }
}