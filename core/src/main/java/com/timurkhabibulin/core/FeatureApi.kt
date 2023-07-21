package com.timurkhabibulin.core


import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface FeatureApi {

    val route: String
    val imageVector: ImageVector
    val resId: Int

    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavController,
        isFullScreen: (Boolean) -> Unit
    )
}
