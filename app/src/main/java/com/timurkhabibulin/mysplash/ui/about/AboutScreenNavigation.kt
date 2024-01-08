package com.timurkhabibulin.mysplash.ui.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val ABOUT_APP_ROUTE = "about_app"

fun NavGraphBuilder.aboutAppScreen(
    isFullScreen: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    composable(ABOUT_APP_ROUTE) {
        isFullScreen(true)
        AboutAppScreen(onBackPressed = onBackClick)
    }
}

fun NavController.navigateToAboutApp() {
    navigate(ABOUT_APP_ROUTE)
}