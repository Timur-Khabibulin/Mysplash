package com.timurkhabibulin.mysplash.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.timurkhabibulin.core.theme.MysplashTheme
import com.timurkhabibulin.topics.ui.TabScreen

@SuppressLint("SuspiciousIndentation")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreen() {
    val bottomNavItems = listOf(
        Screen.Home
    )
    val navController = rememberNavController()

    MysplashTheme {
        Scaffold(
            bottomBar = {
                BottomNavBar(
                    navController,
                    bottomNavItems
                )
            }

        ) { innerPadding ->
            MainScreenNavConfig(navController, innerPadding)
        }
    }
}

@Composable
fun BottomNavBar(
    navController: NavHostController,
    items: List<Screen>
) {
    MysplashTheme {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { screen ->
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
fun MainScreenNavConfig(navController: NavHostController, innerPadding: PaddingValues) {
    MysplashTheme {
        NavHost(
            navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) { TabScreen() }
        }
    }
}