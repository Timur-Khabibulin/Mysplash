package com.timurkhabibulin.topics.topics


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User
import com.timurkhabibulin.photos.R


private const val TOPIC_ROUTE = "topic_route"
private const val TOPIC_ID_ARG = "topicId"
private const val DEFAULT_TOPIC_ID = "defaultTopicId"

object TopicsApi : FeatureApi {
    override val route: String
        get() = TOPIC_ROUTE
    override val iconResId: Int
        get() = R.drawable.topics
    override val titleResId: Int
        get() = R.string.topics

    override fun navigateToFeature(navController: NavController) {
        navController.navigateToTopic()
    }

}

fun NavGraphBuilder.topicScreen(
    isFullScreen: (Boolean) -> Unit,
    onUserClick: (User) -> Unit,
    onPhotoClick: (Photo) -> Unit
) {
    composable(
        "$TOPIC_ROUTE/{${TOPIC_ID_ARG}}",
        arguments = listOf(
            navArgument(TOPIC_ID_ARG) { type = NavType.StringType },
        )
    ) {
        val topicId = it.arguments?.getString(TOPIC_ID_ARG) ?: ""

        isFullScreen(false)
        TopicsScreen(
            onNavigateToUser = onUserClick,
            onNavigateToPhoto = onPhotoClick,
            if (topicId == DEFAULT_TOPIC_ID) null else topicId
        )
    }
}

fun NavController.navigateToTopic(topicId: String = DEFAULT_TOPIC_ID) {
    navigate("$TOPIC_ROUTE/$topicId")
}