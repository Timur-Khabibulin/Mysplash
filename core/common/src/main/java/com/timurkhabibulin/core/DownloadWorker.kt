package com.timurkhabibulin.core

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

//TODO(): Add callbacks with broadcastreciever
@HiltWorker
internal class DownloadWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val FILE_NAME = "FILE_NAME"
        private const val URL = "URL"
        private const val WIDTH = "WIDTH"
        private const val HEIGHT = "HEIGHT"
        fun downloadImage(
            fileName: String,
            url: String?,
            context: Context,
            width: Int,
            height: Int
        ) {
            val request = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setInputData(
                    workDataOf(
                        FILE_NAME to fileName,
                        URL to url,
                        WIDTH to width,
                        HEIGHT to height
                    )
                )
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        try {
            val url = inputData.getString(URL)
            val fileName = inputData.getString(FILE_NAME)
            val width = inputData.getInt(WIDTH, 0)
            val height = inputData.getInt(HEIGHT, 0)//TODO should not be 0

            val bitmap = downloadBitmap(url!!, width, height)

            saveBitmap(fileName!!, bitmap)

            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }

    private suspend fun downloadBitmap(url: String, width: Int, height: Int): Bitmap {

        val request = ImageRequest
            .Builder(applicationContext)
            .data(url)
            .size(width, height)
            .build()

        val result: ImageResult = ImageLoader(applicationContext).execute(request)
        return (result.drawable as BitmapDrawable).bitmap
    }

    private fun saveBitmap(fileName: String, bitmap: Bitmap): Boolean {
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
