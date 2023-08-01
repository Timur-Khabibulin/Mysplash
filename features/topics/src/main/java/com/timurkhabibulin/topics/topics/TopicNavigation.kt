package com.timurkhabibulin.topics.topics

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.core.R
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User


internal const val topicRoute = "topic_route"

object TopicsApi : FeatureApi {
    override val route: String
        get() = topicRoute
    override val imageVector: ImageVector
        get() = Icons.Default.Home
    override val resId: Int
        get() = R.string.home_screen

    override fun navigateToFeature(navController: NavController) {
        navController.navigateToTopic()
    }

}

fun NavGraphBuilder.topicScreen(
    isFullScreen: (Boolean) -> Unit,
    onUserClick: (User) -> Unit,
    onPhotoClick: (Photo) -> Unit
) {
    composable(topicRoute) {
        isFullScreen(false)
        TopicsScreen(
            onNavigateToUser = onUserClick,
            onNavigateToPhoto = onPhotoClick
        )
    }
}

fun NavController.navigateToTopic() {
    navigate(topicRoute)
}