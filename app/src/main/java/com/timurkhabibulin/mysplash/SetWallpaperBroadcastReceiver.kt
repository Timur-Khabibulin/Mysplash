package com.timurkhabibulin.mysplash

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.timurkhabibulin.core.WallpaperUtil
import com.timurkhabibulin.domain.ImageUtils

class SetWallpaperBroadcastReceiver(
    private val wallpaperUtil: WallpaperUtil
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (context != null) {
                val uri = it.getStringExtra(ImageUtils.URI_PARAM)?.toUri()
                wallpaperUtil.setWallpaper(uri, context)
            }
        }
    }
}