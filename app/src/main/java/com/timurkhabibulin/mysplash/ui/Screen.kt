package com.timurkhabibulin.mysplash.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.timurkhabibulin.core.R.string.home_screen

sealed class Screen(val route: String, val imageVector: ImageVector, @StringRes val resId: Int) {
    object Home : Screen("home", Icons.Default.Home, home_screen)
//    object Search : Screen("search", Icons.Default.Search, search_screen)
}