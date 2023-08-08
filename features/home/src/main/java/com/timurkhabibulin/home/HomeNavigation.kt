package com.timurkhabibulin.home


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.Topic
import com.timurkhabibulin.domain.entities.User

internal const val homeRoute = "home_route"

object HomeApi : FeatureApi {
    override val route: String
        get() = homeRoute
    override val iconResId: Int
        get() = R.drawable.home
    override val titleResId: Int
        get() = R.string.home

    override fun navigateToFeature(navController: NavController) {
        navController.navigateToHome()
    }

}

fun NavGraphBuilder.homeScreen(
    isFullScreen: (Boolean) -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    onTopicClick: (Topic) -> Unit,
) {
    composable(homeRoute) {
        isFullScreen(false)
        HomeScreen(
            onPhotoClick = onPhotoClick,
            onUserClick = onUserClick,
            onTopicClick = onTopicClick
        )
    }
}

fun NavController.navigateToHome() {
    navigate(homeRoute)
}