package com.timurkhabibulin.core

import androidx.navigation.NavController

interface FeatureApi {

    val route: String

    val iconResId: Int

    val titleResId: Int

    fun navigateToFeature(navController: NavController)
}