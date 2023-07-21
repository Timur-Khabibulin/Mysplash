package com.timurkhabibulin.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Urls(
    val thumb: String? = null,
    val small: String? = null,
    val medium: String? = null,
    val regular: String? = null,
    val large: String? = null,
    val full: String? = null,
    val raw: String? = null
) : Parcelable
