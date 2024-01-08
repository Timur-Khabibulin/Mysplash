package com.timurkhabibulin.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timurkhabibulin.data.db.entities.PhotoDbEntity

@Database(
    entities = [PhotoDbEntity::class],
    version = 1
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}