package com.timurkhabibulin.mysplash.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.timurkhabibulin.home.HomeApi
import com.timurkhabibulin.home.homeScreen
import com.timurkhabibulin.mysplash.ui.about.aboutAppScreen
import com.timurkhabibulin.mysplash.ui.about.navigateToAboutApp
import com.timurkhabibulin.search.searchScreen
import com.timurkhabibulin.topics.photo.navigateToPhoto
import com.timurkhabibulin.topics.photo.photoScreen
import com.timurkhabibulin.topics.topics.navigateToTopic
import com.timurkhabibulin.topics.topics.topicScreen
import com.timurkhabibulin.user.collections.collectionPhotosScreen
import com.timurkhabibulin.user.collections.navigateToCollectionPhotos
import com.timurkhabibulin.user.favorites.favoriteScreen
import com.timurkhabibulin.user.user.navigateToUser
import com.timurkhabibulin.user.user.userScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    isFullScreen: (Boolean) -> Unit
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = HomeApi.route
    ) {
        homeScreen(
            isFullScreen = isFullScreen,
            onUserClick = { user -> navController.navigateToUser(user.username) },
            onPhotoClick = { photo -> navController.navigateToPhoto(photo.id) },
            onTopicClick = { topic -> navController.navigateToTopic(topic.id) }
        )

        topicScreen(
            isFullScreen = isFullScreen,
            onUserClick = { user -> navController.navigateToUser(user.username) },
            onPhotoClick = { photo -> navController.navigateToPhoto(photo.id) }
        )

        searchScreen(
            isFullScreen = isFullScreen,
            onUserClick = { user -> navController.navigateToUser(user.username) },
            onPhotoClick = { photo -> navController.navigateToPhoto(photo.id) },
            onCollectionClick = { id -> navController.navigateToCollectionPhotos(id) }
        )

        favoriteScreen(
            isFullScreen = isFullScreen,
            onPhotoClick = { photo -> navController.navigateToPhoto(photo.id) },
            onMenuClick = { navController.navigateToAboutApp() }
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

        aboutAppScreen(
            isFullScreen = isFullScreen,
            onBackClick = navController::navigateUp
        )
    }
}