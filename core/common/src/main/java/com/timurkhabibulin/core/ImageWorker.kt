package com.timurkhabibulin.core

import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.timurkhabibulin.core.utils.ImageDownloaderUtil
import com.timurkhabibulin.domain.ImageUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
internal class ImageWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val FILE_NAME = "FILE_NAME"
        private const val URL = "URL"
        private const val WIDTH = "WIDTH"
        private const val HEIGHT = "HEIGHT"
        private const val SET_AS_WALLPAPER = "SET_AS_WALLPAPER"
        fun downloadImage(
            fileName: String,
            url: String?,
            context: Context,
            width: Int,
            height: Int,
            setAsWallpaper: Boolean
        ) {
            val request = OneTimeWorkRequestBuilder<ImageWorker>()
                .setInputData(
                    workDataOf(
                        FILE_NAME to fileName,
                        URL to url,
                        WIDTH to width,
                        HEIGHT to height,
                        SET_AS_WALLPAPER to setAsWallpaper
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
            val height = inputData.getInt(HEIGHT, 0)
            val setAsWallpaper = inputData.getBoolean(SET_AS_WALLPAPER, false)

            val uri = ImageDownloaderUtil.download(
                context,
                url!!,
                fileName!!,
                width,
                height
            )

            if (setAsWallpaper) {
                context.sendBroadcast(
                    Intent(ImageUtils.MYSPLASH_SET_AS_WALLPAPER_ACTION).putExtra(
                        ImageUtils.URI_PARAM, uri.toString()
                    )
                )
            }

            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }
}
