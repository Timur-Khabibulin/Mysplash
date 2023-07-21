package com.timurkhabibulin.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val username: String,
    val name: String,
    val portfolio_url: String?,
    val bio: String?,
    val location: String?,
    val total_likes: Int,
    val total_photos: Int,
    val total_collection: Int,
    val profile_image: Urls,
    val links: Links
) : Parcelable {
    companion object {
        val DefaultUser = User(
            "QPxL2MGqfrw",
            "exampleuser",
            "Joe Example",
            null,
            null,
            null,
            5,
            10,
            7,
            Urls(),
            Links()
        )
    }
}
