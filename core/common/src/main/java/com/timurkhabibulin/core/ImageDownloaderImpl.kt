package com.timurkhabibulin.core

import android.content.Context
import com.timurkhabibulin.domain.ImageDownloader
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageDownloaderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ImageDownloader {
    override fun download(fileName: String, url: String?, width: Int, height: Int) {
        DownloadWorker.downloadImage(fileName, url, context, width, height)
    }
}
