package com.timurkhabibulin.mysplash.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.core.theme.MysplashTheme
import com.timurkhabibulin.topics.TopicsApi


@Composable
internal fun MainScreen(mainScreenViewModel: MainScreenViewModel = viewModel()) {

    val navController = rememberNavController()
    val isFullScreen by mainScreenViewModel.isFullScreen.collectAsState()

    MysplashTheme {
        Scaffold(
            bottomBar = {
                if (!isFullScreen)
                    BottomNavBar(
                        mainScreenViewModel.features,
                        navController
                    )
            }

        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                MainScreenNavConfig(
                    mainScreenViewModel.features,
                    navController
                ) { isFullScreen ->
                    mainScreenViewModel.changeScreenMode(isFullScreen)
                }
            }
        }
    }
}

@Composable
internal fun BottomNavBar(
    features: Set<FeatureApi>,
    navController: NavHostController
) {
    MysplashTheme {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            features.forEach { screen ->
                NavigationBarItem(
                    label = { Text(stringResource(screen.resId)) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        if (currentRoute != screen.route)
                            navController.navigate(screen.route)
                    },
                    icon = {
                        Icon(
                            screen.imageVector,
                            "Menu icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    }
}

@Composable
internal fun MainScreenNavConfig(
    features: Set<FeatureApi>,
    navController: NavHostController,
    isFullScreen: (Boolean) -> Unit
) {
    MysplashTheme {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = TopicsApi.Route
        ) {
            features.forEach {
                it.registerGraph(this, navController, isFullScreen)
            }
        }
    }
}