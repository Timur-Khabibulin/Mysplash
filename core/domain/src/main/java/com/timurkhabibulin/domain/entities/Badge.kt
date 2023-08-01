package com.timurkhabibulin.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Badge(
    val title: String,
    val primary: Boolean,
    val slug: String,
    val link: String
) : Parcelable {
    companion object {
        val Default = Badge(
            "Book contributor",
            true,
            "book-contributor",
            "https://book.unsplash.com"
        )
    }
}