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
    val followers_count: Int,
    val following_count: Int,
    val profile_image: Urls,
    val badge: Badge?,
    val links: Links
) : Parcelable {
    companion object {
        val DefaultUser = User(
            "QPxL2MGqfrw",
            "exampleuser",
            "Joe Example",
            null,
            "From epic drone shots to inspiring moments" +
                    " in nature — submit your best desktop and mobile backgrounds.",
            "Location",
            5,
            10,
            7,
            3,
            2,
            Urls(),
            Badge.Default,
            Links()
        )
    }
}
