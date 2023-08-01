package com.timurkhabibulin.mysplash.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.timurkhabibulin.topics.topics.TopicsApi
import com.timurkhabibulin.topics.photo.navigateToPhoto
import com.timurkhabibulin.topics.photo.photoScreen
import com.timurkhabibulin.topics.topics.topicScreen
import com.timurkhabibulin.user.user.navigateToUser
import com.timurkhabibulin.user.collections.collectionPhotosScreen
import com.timurkhabibulin.user.collections.navigateToCollectionPhotos
import com.timurkhabibulin.user.user.userScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    isFullScreen: (Boolean) -> Unit
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = TopicsApi.route
    ) {
        topicScreen(
            isFullScreen = isFullScreen,
            onUserClick = { user -> navController.navigateToUser(user.username) },
            onPhotoClick = { photo -> navController.navigateToPhoto(photo.id) }
        )

        photoScreen(
            isFullScreen = isFullScreen,
            onUserClick = { user -> navController.navigateToUser(user.username) },
            onBackClick = navController::navigateUp
        )

        userScreen(
            isFullScreen = isFullScreen,
            onBackClick = navController::navigateUp,
            onPhotoClick = { photo -> navController.navigateToPhoto(photo.id) },
            onCollectionClick = { id -> navController.navigateToCollectionPhotos(id) }
        )

        collectionPhotosScreen(
            isFullScreen = isFullScreen,
            onBackClick = navController::navigateUp,
            onPhotoClick = { photo -> navController.navigateToPhoto(photo.id) },
            onUserClick = { user -> navController.navigateToUser(user.username) }
        )
    }
}