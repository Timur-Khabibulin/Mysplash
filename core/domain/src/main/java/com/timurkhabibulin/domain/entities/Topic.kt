package com.timurkhabibulin.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    val id: String,
    val title: String,
    val description: String,
    override val links: Links,
    val cover_photo: Photo
) : Parcelable,Linkable {
    companion object {
        val Default = Topic(
            "123123",
            "Some topic",
            "Very interesting description of this super magic topic",
            Links(),
            Photo.Default
        )
    }
}
