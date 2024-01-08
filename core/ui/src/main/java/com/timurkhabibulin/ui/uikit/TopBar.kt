package com.timurkhabibulin.ui.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.timurkhabibulin.ui.R

@Preview
@Composable
fun TopBarPreview() {
    TopBar(
        onBackPressed = {},
        title = "Title",
        onOpenInBrowser = {}
    )
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackPressed: () -> Unit,
    onOpenInBrowser: () -> Unit
) {
    TopBar(
        modifier = modifier,
        title = title,
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back icon",
                Modifier.clickable { onBackPressed() },
                tint = MaterialTheme.colorScheme.primary
            )
        },
        actionIcon = {
            Icon(
                modifier = Modifier.clickable { onOpenInBrowser() },
                painter = painterResource(R.drawable.link_external_02),
                contentDescription = "Open in browser icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actionIcon: (@Composable () -> Unit)? = null
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 25.dp),
        horizontalArrangement = if (actionIcon == null) Arrangement.spacedBy(20.dp) else Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        navigationIcon?.invoke()
        title?.let {
            Text(text = it, style = MaterialTheme.typography.titleLarge)
        }
        actionIcon?.invoke()
    }
}