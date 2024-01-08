package com.timurkhabibulin.user.favorites

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.user.R

private const val FAVORITE_ROUTE = "favorite_route"

object FavoriteApi : FeatureApi {
    override val route: String
        get() = FAVORITE_ROUTE
    override val iconResId: Int
        get() = R.drawable.bookmark_border
    override val titleResId: Int
        get() = R.string.favorite

    override fun navigateToFeature(navController: NavController) {
        navController.navigateToFavorite()
    }
}

fun NavGraphBuilder.favoriteScreen(
    isFullScreen: (Boolean) -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onMenuClick: () -> Unit
) {
    composable(FAVORITE_ROUTE) {
        isFullScreen(false)
        FavoritesScreen(
            onPhotoClick = onPhotoClick,
            onMenuClick = onMenuClick
        )
    }
}

fun NavController.navigateToFavorite() {
    navigate(FAVORITE_ROUTE)
}