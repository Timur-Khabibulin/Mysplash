package com.timurkhabibulin.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(
    val id: String,
    val title: String,
    val description: String?,
    val published_at: String,
    val last_collected_at: String,
    val updated_at: String,
    val featured: Boolean,
    val total_photos: Int,
    val private: Boolean,
    val share_key: String,
    val cover_photo: Photo?,
    val user: User?,
    override val links: Links
) : Parcelable,Linkable {
    companion object {
        val Default = Collection(
            "206",
            "Makers: Cat and Ben",
            "Behind-the-scenes photos from the Makers interview with designers Cat Noone and Benedikt Lehnert.",
            "2016-01-12T18:16:09-05:00",
            "2016-06-02T13:10:03-04:00",
            "2016-07-10T11:00:01-05:00",
            false,
            12,
            false,
            "312d188df257b957f8b86d2ce20e4766",
            Photo.Default,
            User.DefaultUser,
            Links()
        )
    }
}
