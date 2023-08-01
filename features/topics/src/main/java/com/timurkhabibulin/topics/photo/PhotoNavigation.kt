package com.timurkhabibulin.topics.photo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.timurkhabibulin.domain.entities.User

internal const val photoRoute = "photo_route"
internal const val photoIdArg = "photoId"

fun NavGraphBuilder.photoScreen(
    isFullScreen: (Boolean) -> Unit,
    onUserClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    composable(
        "$photoRoute/{$photoIdArg}",
        arguments = listOf(
            navArgument(photoIdArg) { type = NavType.StringType },
        )
    ) {
        val photoId = it.arguments?.getString(photoIdArg) ?: ""

        isFullScreen(true)
        PhotoScreen(
            photoID = photoId,
            onBackPressed = onBackClick,
            onNavigateToUser = onUserClick
        )
    }
}

fun NavController.navigateToPhoto(photoId: String) {
    navigate("$photoRoute/$photoId")
}