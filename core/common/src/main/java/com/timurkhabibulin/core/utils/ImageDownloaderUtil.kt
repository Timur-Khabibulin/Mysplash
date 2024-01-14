package com.timurkhabibulin.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult

internal class ImageDownloaderUtil {
    companion object {
        suspend fun download(
            applicationContext: Context,
            url: String,
            fileName: String,
            width: Int,
            height: Int
        ): Uri? {
            val bitmap = downloadBitmap(applicationContext, url, width, height)
            return saveBitmap(applicationContext, fileName, bitmap)
        }

        private suspend fun downloadBitmap(
            applicationContext: Context,
            url: String,
            width: Int,
            height: Int
        ): Bitmap {
            val request = ImageRequest
                .Builder(applicationContext)
                .data(url)
                .size(width, height)
                .build()

            val result: ImageResult = ImageLoader(applicationContext).execute(request)
            return (result.drawable as BitmapDrawable).bitmap
        }

        private fun saveBitmap(
            applicationContext: Context,
            fileName: String,
            bitmap: Bitmap
        ): Uri? {
            val uri = createDirectory(fileName, applicationContext)

            uri?.let {
                applicationContext.contentResolver.openOutputStream(it)?.let { stream ->
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        stream
                    )
                }
            }

            return uri
        }
    }
}