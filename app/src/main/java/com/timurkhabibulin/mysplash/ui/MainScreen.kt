package com.timurkhabibulin.mysplash.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.mysplash.navigation.BOTTOM_BAR_ITEMS
import com.timurkhabibulin.mysplash.navigation.MainNavHost


@Composable
internal fun MainScreen(mainScreenViewModel: MainScreenViewModel = viewModel()) {

    val navController = rememberNavController()
    val isFullScreen by mainScreenViewModel.isFullScreen.collectAsState()

    Scaffold(
        bottomBar = {
            if (!isFullScreen)
                BottomNavBar(
                    BOTTOM_BAR_ITEMS,
                    navController
                )
        }

    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            MainNavHost(
                navController = navController,
                isFullScreen = { mainScreenViewModel.changeScreenMode(it) })
        }
    }
}

@Composable
internal fun BottomNavBar(
    features: Set<FeatureApi>,
    navController: NavHostController
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        features.forEach { feature ->
            NavigationBarItem(
                label = { Text(stringResource(feature.resId)) },
                selected = currentRoute == feature.route,
                onClick = {
                    if (currentRoute != feature.route)
                        feature.navigateToFeature(navController)
                },
                icon = {
                    Icon(
                        feature.imageVector,
                        "Menu icon"
                    )
                }
            )
        }
    }
}