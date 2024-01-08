package com.timurkhabibulin.mysplash.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.timurkhabibulin.mysplash.BuildConfig
import com.timurkhabibulin.mysplash.R
import com.timurkhabibulin.ui.theme.MysplashTheme
import com.timurkhabibulin.ui.uikit.TopBar

@Composable
internal fun AboutAppScreen(
    onBackPressed: () -> Unit,
    viewModel: AboutAppViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val appLabel = context.packageManager.getApplicationLabel(context.applicationInfo).toString()
    AboutAppScreen(
        onBackPressed = onBackPressed,
        onSourceCodeClick = { viewModel.openGitHub(context) },
        onDeveloperClick = { viewModel.openDeveloper(context) },
        appName = appLabel,
        appVersion = BuildConfig.VERSION_NAME
    )
}

@Preview
@Composable
fun AboutAppScreenPreview() {
    MysplashTheme {
        AboutAppScreen({}, {}, {}, "Mysplash", "1.1")
    }
}

@Composable
private fun AboutAppScreen(
    onBackPressed: () -> Unit,
    onSourceCodeClick: () -> Unit,
    onDeveloperClick: () -> Unit,
    appName: String,
    appVersion: String
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.about_app),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back icon",
                        Modifier.clickable { onBackPressed() },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AppInfo(appName, appVersion)
            Items(
                onDeveloperClick = onDeveloperClick,
                onSourceCodeClick = onSourceCodeClick
            )
        }
    }
}

@Composable
private fun AppInfo(
    appName: String,
    appVersion: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.app_icon)
        )
        Text(
            text = appName,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = appVersion,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun Items(
    onSourceCodeClick: () -> Unit,
    onDeveloperClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Item(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDeveloperClick() },
            icon = {
                Icon(
                    modifier = it,
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            },
            name = stringResource(R.string.developer),
            title = stringResource(R.string.timur_khabibulin)
        )
        Item(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSourceCodeClick() },
            icon = {
                Icon(
                    modifier = it,
                    painter = painterResource(id = R.drawable.github_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            },
            name = stringResource(R.string.github),
            title = stringResource(R.string.source_code)
        )
    }
}


@Composable
private fun Item(
    modifier: Modifier = Modifier,
    icon: (@Composable (Modifier) -> Unit)? = null,
    name: String,
    title: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.invoke(Modifier.size(25.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primaryContainer
            )
            Text(text = title, style = MaterialTheme.typography.titleSmall)
        }
    }
}