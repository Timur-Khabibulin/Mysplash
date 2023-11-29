package com.timurkhabibulin.domain

interface ImageDownloader {
    fun download(fileName: String, url: String?, width: Int, height: Int)
}
