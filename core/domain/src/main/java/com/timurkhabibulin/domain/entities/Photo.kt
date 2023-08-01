package com.timurkhabibulin.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Photo(
    val id: String,
    val created_at: String,
    val width: Int,
    val height: Int,
    val color: String? = "#000000",
    val downloads: Int,
    val likes: Int,
    val description: String?,
    val exif: Exif?,
    val urls: Urls,
    val links: Links,
    val user: User
) : Parcelable {
    companion object {
        val Default = Photo(
            "Dwu85P9SOIk",
            "2016-05-03T11:00:28-04:00",
            2448,
            3264,
            "#6E633A",
            1345,
            24,
            "A man drinking a coffee.",
            Exif.Default,
            Urls(),
            Links(),
            User.DefaultUser
        )
    }
}