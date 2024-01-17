package com.timurkhabibulin.domain

import com.timurkhabibulin.domain.entities.Photo

interface ImageUtils {
    companion object {
        const val MYSPLASH_SET_AS_WALLPAPER_ACTION = "MYSPLASH_SET_AS_WALLPAPER_ACTION"
        const val MYSPLASH_DOWNLOAD_START = "MYSPLASH_DOWNLOAD_START"
        const val MYSPLASH_DOWNLOAD_END = "MYSPLASH_DOWNLOAD_END"
        const val URI_PARAM = "uri"
        fun getFileNameFromPhoto(photo: Photo) = "${photo.user.username}-${photo.id}.jpeg"
    }

    suspend fun download(photo: Photo, url: String?, width: Int, height: Int)

    suspend fun cropAndSetAsWallpaper(photo: Photo, url: String?, width: Int, height: Int)
}
