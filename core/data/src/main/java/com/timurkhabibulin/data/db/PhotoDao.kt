package com.timurkhabibulin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timurkhabibulin.data.db.entities.PhotoDbEntity

@Dao
internal interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoDbEntity>)

    @Query("SELECT * FROM Favorites_photos LIMIT :limit OFFSET :offset")
    suspend fun getPagedPhotos(limit: Int, offset: Int): List<PhotoDbEntity>

    @Query("DELETE FROM Favorites_photos")
    suspend fun clearAll()

    @Insert
    suspend fun insert(photo: PhotoDbEntity)

    @Query("SELECT * FROM Favorites_photos WHERE uid LIKE :id")
    fun findById(id: String): PhotoDbEntity?

    @Query("DELETE FROM Favorites_photos WHERE uid LIKE :id")
    fun remove(id:String)
}