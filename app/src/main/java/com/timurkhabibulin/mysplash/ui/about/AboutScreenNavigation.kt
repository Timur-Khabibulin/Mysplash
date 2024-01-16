package com.timurkhabibulin.mysplash.ui.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.timurkhabibulin.core.utils.TrackScreen

private const val ABOUT_APP_ROUTE = "about_app"
private const val SCREEN_NAME = "aboutApp"

fun NavGraphBuilder.aboutAppScreen(
    isFullScreen: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    composable(ABOUT_APP_ROUTE) {
        isFullScreen(true)
        AboutAppScreen(onBackPressed = onBackClick)
        TrackScreen(SCREEN_NAME)
    }
}

fun NavController.navigateToAboutApp() {
    navigate(ABOUT_APP_ROUTE)
}