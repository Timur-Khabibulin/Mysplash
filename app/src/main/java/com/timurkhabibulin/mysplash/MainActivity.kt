package com.timurkhabibulin.mysplash

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.timurkhabibulin.core.WallpaperUtil
import com.timurkhabibulin.domain.ImageUtils
import com.timurkhabibulin.mysplash.ui.MainScreen
import com.timurkhabibulin.ui.theme.MysplashTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var wallpaperUtil: WallpaperUtil
    private lateinit var setWallpaperBroadcastReceiver: SetWallpaperBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWallpaperBroadcastReceiver = SetWallpaperBroadcastReceiver(wallpaperUtil)
        registerReceiver(
            setWallpaperBroadcastReceiver,
            IntentFilter(ImageUtils.MYSPLASH_SET_AS_WALLPAPER_ACTION),
            RECEIVER_NOT_EXPORTED
        )

        setContent {
            MysplashTheme {
                MainScreen()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(setWallpaperBroadcastReceiver)
    }
}




