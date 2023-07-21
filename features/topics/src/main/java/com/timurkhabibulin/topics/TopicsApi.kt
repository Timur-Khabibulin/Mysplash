package com.timurkhabibulin.topics

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.core.R.string.home_screen

interface TopicsApi : FeatureApi {
    companion object {
        const val Route: String = "Topics"
    }

    override val route: String
        get() = "Topics"
    override val imageVector: ImageVector
        get() = Icons.Default.Home
    override val resId: Int
        get() = home_screen
}