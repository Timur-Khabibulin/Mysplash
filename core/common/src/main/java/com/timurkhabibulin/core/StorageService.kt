package com.timurkhabibulin.core

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class StorageService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun saveBitmap(fileName: String, bitmap: Bitmap): Boolean {
        val outputStream =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
                createDir(fileName)
            else createDirLegacy(fileName)

        return outputStream?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        } ?: false
    }

    private fun createDir(fileName: String): OutputStream? {
        val resolver = context.contentResolver

        context.contentResolver.also {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.TITLE, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            return resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )?.let {
                resolver.openOutputStream(it)
            }
        }
    }

    private fun createDirLegacy(fileName: String): OutputStream {
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, fileName)
        return FileOutputStream(image)
    }
}