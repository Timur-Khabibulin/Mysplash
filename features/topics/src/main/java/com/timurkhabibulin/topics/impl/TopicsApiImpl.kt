package com.timurkhabibulin.topics.impl

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.timurkhabibulin.domain.entities.Photo
import com.timurkhabibulin.topics.TopicsApi
import com.timurkhabibulin.topics.impl.ui.PhotoInfoScreen
import com.timurkhabibulin.topics.impl.ui.TopicsScreen

internal class TopicsApiImpl : TopicsApi {
    private val detailsRoute = "$route/details"
    private val photoRoute = "$detailsRoute/photo"

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        isFullScreen: (Boolean) -> Unit
    ) {
        navGraphBuilder.composable(route) {
            isFullScreen(false)
            TopicsScreen(
                onNavigateToUser = {},
                onNavigateToPhoto = { photo ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("photo", photo)
                    navController.navigate(photoRoute)
                }
            )
        }

        navGraphBuilder.navigation(startDestination = photoRoute, route = detailsRoute) {
            composable(photoRoute) {
                isFullScreen(true)
                val photo =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Photo>("photo")
                PhotoInfoScreen(photo ?: Photo.Default) {
                    navController.navigateUp()
                }
            }
        }
    }
}