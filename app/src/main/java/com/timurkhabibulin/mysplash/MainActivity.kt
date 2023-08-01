package com.timurkhabibulin.mysplash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.timurkhabibulin.mysplash.ui.MainScreen
import com.timurkhabibulin.ui.theme.MysplashTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MysplashTheme {
                MainScreen()
            }
        }
    }
}




