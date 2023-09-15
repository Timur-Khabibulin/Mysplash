package com.timurkhabibulin.ui.uikit

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.ui.R
import com.timurkhabibulin.ui.theme.MysplashTheme

@Preview
@Composable
internal fun TopicCardPreview() {
    MysplashTheme {
        TopicCard(Topic.Default) {}
    }
}

@Composable
fun TopicCard(
    topic: Topic,
    onTopicClick: (Topic) -> Unit,
) {
    Column(
        Modifier
            .clickable { onTopicClick(topic) }
            .testTag("topicPreview"),
        verticalArrangement = Arrangement.spacedBy(
            5.dp, Alignment.Top
        ),
        horizontalAlignment = Alignment.Start,
    ) {
        Box(
            Modifier
                .width(200.dp)
                .height(120.dp), contentAlignment = Alignment.BottomStart
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(topic.cover_photo.urls.small).diskCachePolicy(CachePolicy.ENABLED)
                    .placeholder(ColorDrawable(topic.cover_photo.color!!.toColorInt()))
                    .crossfade(250).build(),
                contentDescription = "topicImage",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(size = 10.dp)),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color(0x66000000), BlendMode.Darken)
            )

            Text(
                modifier = Modifier.padding(10.dp),
                text = topic.title,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Icon(
            modifier = Modifier.padding(start = 10.dp),
            painter = painterResource(R.drawable.long_arrow),
            contentDescription = "arrow",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}