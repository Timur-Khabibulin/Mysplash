package com.timurkhabibulin.topics.photo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.timurkhabibulin.core.utils.TrackScreen
import com.timurkhabibulin.domain.entities.User

private const val PHOTO_ROUTE = "photo_route"
private const val PHOTO_ID_ARG = "photoId"
internal const val SCREEN_NAME = "Photo"

fun NavGraphBuilder.photoScreen(
    isFullScreen: (Boolean) -> Unit,
    onUserClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    composable(
        "$PHOTO_ROUTE/{$PHOTO_ID_ARG}",
        arguments = listOf(
            navArgument(PHOTO_ID_ARG) { type = NavType.StringType },
        )
    ) {
        val photoId = it.arguments?.getString(PHOTO_ID_ARG) ?: ""

        isFullScreen(true)
        PhotoScreen(
            photoID = photoId,
            onBackPressed = onBackClick,
            onNavigateToUser = onUserClick
        )
        TrackScreen(SCREEN_NAME)
    }
}

fun NavController.navigateToPhoto(photoId: String) {
    navigate("$PHOTO_ROUTE/$photoId")
}