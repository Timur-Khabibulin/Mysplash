package com.timurkhabibulin.core.utils

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WallpaperUtil @Inject constructor(
    private val coroutineScope: CoroutineScope
) {
    fun setWallpaper(uri: Uri?, context: Context) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        try {
            context.startActivity(
                wallpaperManager.getCropAndSetWallpaperIntent(uri)
            )
        } catch (exception: Exception) {
            if (uri != null) {
                coroutineScope.launch {
                    val bitmap = getBitmapFromUri(uri, context)
                    wallpaperManager.setBitmap(bitmap)
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri, context: Context): Bitmap {
        val parcelFileDescriptor =
            context.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }
}

