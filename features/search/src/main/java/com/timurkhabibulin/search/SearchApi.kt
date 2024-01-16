package com.timurkhabibulin.search

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.core.utils.TrackScreen
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.domain.entities.User

private const val SEARCH_ROUTE = "search_route"

object SearchApi : FeatureApi {
    override val route: String
        get() = SEARCH_ROUTE
    override val iconResId: Int
        get() = R.drawable.search_sm
    override val titleResId: Int
        get() = R.string.search

    override fun navigateToFeature(navController: NavController) {
        navController.navigateToSearch()
    }
}

fun NavGraphBuilder.searchScreen(
    isFullScreen: (Boolean) -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onUserClick: (User) -> Unit,
    onCollectionClick: (String) -> Unit
) {
    composable(SEARCH_ROUTE) {
        isFullScreen(false)

        SearchScreen(
            onPhotoClick = onPhotoClick,
            onUserClick = onUserClick,
            onCollectionClick = onCollectionClick
        )
        TrackScreen(name = stringResource(id = SearchApi.titleResId))
    }
}

fun NavController.navigateToSearch() {
    navigate(SEARCH_ROUTE)
}