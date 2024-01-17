package com.timurkhabibulin.mysplash

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.timurkhabibulin.core.utils.WallpaperUtil
import com.timurkhabibulin.domain.ImageUtils

class SetWallpaperBroadcastReceiver(
    private val wallpaperUtil: WallpaperUtil
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            intent.action?.let { action ->
                when (action) {
                    ImageUtils.MYSPLASH_SET_AS_WALLPAPER_ACTION -> {
                        if (context != null) {
                            val uri = intent.getStringExtra(ImageUtils.URI_PARAM)?.toUri()
                            wallpaperUtil.setWallpaper(uri, context)
                        } else {
                        }
                    }

                    ImageUtils.MYSPLASH_DOWNLOAD_START -> {
                        context?.let { ctx ->
                            Toast.makeText(
                                ctx,
                                ctx.getString(R.string.download_start),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    ImageUtils.MYSPLASH_DOWNLOAD_END -> {
                        context?.let { ctx ->
                            Toast.makeText(
                                ctx,
                                ctx.getString(R.string.download_end),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}