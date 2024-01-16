package com.timurkhabibulin.core.utils

import android.content.Context
import com.timurkhabibulin.domain.ImageUtils
import com.timurkhabibulin.domain.entities.Photo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageUtilsImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ImageUtils {
    override suspend fun download(photo: Photo, url: String?, width: Int, height: Int) {
        ImageWorker.downloadImage(
            ImageUtils.getFileNameFromPhoto(photo),
            url,
            context,
            width,
            height,
            false
        )
    }

    override suspend fun cropAndSetAsWallpaper(
        photo: Photo,
        url: String?,
        width: Int,
        height: Int
    ) {
        ImageWorker.downloadImage(
            ImageUtils.getFileNameFromPhoto(photo),
            url,
            context,
            width,
            height,
            true
        )
    }
}
