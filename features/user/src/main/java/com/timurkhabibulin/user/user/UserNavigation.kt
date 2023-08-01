package com.timurkhabibulin.user.user

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.user.R

internal const val userRoute = "user_route"
internal const val userNameArg = "username"

object UserApi : FeatureApi {
    override val route: String
        get() = userRoute
    override val imageVector: ImageVector
        get() = Icons.Default.AccountCircle
    override val resId: Int
        get() = R.string.user

    override fun navigateToFeature(navController: NavController) {
        navController.navigateToUser("")
    }

}

fun NavGraphBuilder.userScreen(
    isFullScreen: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onCollectionClick: (String) -> Unit
) {
    composable(
        "$userRoute/{$userNameArg}",
        arguments = listOf(
            navArgument(userNameArg) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        val username = it.arguments?.getString(userNameArg) ?: ""

        isFullScreen(username.isNotEmpty())
        UserScreen(
            username = username,
            onPhotoClick = onPhotoClick,
            onBackPressed = onBackClick,
            onCollectionClick = onCollectionClick
        )
    }
}


fun NavController.navigateToUser(userName: String) {
    navigate("$userRoute/$userName")
}