package com.timurkhabibulin.core.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

internal fun createDirectory(fileName: String, context: Context): Uri? {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
        return createDir(fileName, context)
    else createDirLegacy(fileName, context)
}

private fun createDir(fileName: String, context: Context): Uri? {
    val resolver = context.contentResolver

    resolver.also {
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
        )
    }
}

private fun createDirLegacy(fileName: String, context: Context): Uri {
    val imagesDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val image = File(imagesDir, fileName)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        image
    )
}
