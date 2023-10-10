package com.timurkhabibulin.core

interface ImageDownloader {
    fun download(fileName: String, url: String?, width: Int, height: Int)
}