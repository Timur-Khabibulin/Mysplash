package com.timurkhabibulin.core

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

interface FeatureApi {

    val route: String

    val imageVector: ImageVector

    val resId: Int

    fun navigateToFeature(navController: NavController)
}