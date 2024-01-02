package com.timurkhabibulin.domain

interface ImageDownloader {
    suspend fun download(fileName: String, url: String?, width: Int, height: Int)
}
