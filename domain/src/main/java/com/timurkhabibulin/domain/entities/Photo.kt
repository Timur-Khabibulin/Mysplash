package com.timurkhabibulin.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Photo(
    val id: String,
    val created_at: String,
    val width: Int,
    val height: Int,
    val blur_hash: String,
    val color: String? = "#000000",
    val likes: Int,
    val description: String?,
    val urls: Urls,
    val links: Links,
    val user: User
) : Parcelable