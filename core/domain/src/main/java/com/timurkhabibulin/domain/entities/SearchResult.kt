package com.timurkhabibulin.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResult<T : Parcelable>(
    val total: Int,
    val total_pages: Int,
    val results: List<T>
) : Parcelable
