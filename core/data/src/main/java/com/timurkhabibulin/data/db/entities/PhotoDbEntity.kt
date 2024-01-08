package com.timurkhabibulin.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorites_photos")
internal data class PhotoDbEntity(
    @PrimaryKey val uid: String,
)