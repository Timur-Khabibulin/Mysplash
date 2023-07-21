package com.timurkhabibulin.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exif(
    val make: String,
    val model: String,
    val name: String,
    val exposure_time: String,
    val aperture: String,
    val focal_length: String,
    val iso: Int
) : Parcelable {
    companion object {
        val Default = Exif(
            "Canon",
            "Canon EOS 40D",
            "Canon, EOS 40D",
            "0.1112",
            "4.970854",
            "37",
            100
        )
    }
}
