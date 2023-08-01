package com.timurkhabibulin.user.collections

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User

internal const val collectionPhotosRoute = "collection_photos_route"
internal const val collectionIdArg = "collectionId"

fun NavGraphBuilder.collectionPhotosScreen(
    isFullScreen: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit
) {
    composable(
        route = "$collectionPhotosRoute/{$collectionIdArg}",
        arguments = listOf(
            navArgument(collectionIdArg) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        val id = it.arguments?.getString(collectionIdArg) ?: ""

        isFullScreen(true)
        CollectionPhotosScreen(
            collectionId = id,
            onBackPressed = onBackClick,
            onPhotoClick = onPhotoClick,
            onUserClick = onUserClick
        )
    }
}

fun NavController.navigateToCollectionPhotos(collectionId: String) {
    navigate("$collectionPhotosRoute/$collectionId")
}