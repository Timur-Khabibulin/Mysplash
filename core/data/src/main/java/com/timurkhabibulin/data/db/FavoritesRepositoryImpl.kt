package com.timurkhabibulin.data.db

import com.timurkhabibulin.data.db.entities.PhotoDbEntity
import com.timurkhabibulin.domain.me.FavoritesRepository

internal class FavoritesRepositoryImpl(
    private val photoDao: PhotoDao
) : FavoritesRepository {
    override suspend fun getFavoritePhotos(limit: Int, offset: Int): List<String> {
        return photoDao.getPagedPhotos(limit, offset).map { it.uid }
    }

    override suspend fun isFavorite(id: String): Boolean {
        return photoDao.findById(id) != null
    }

    override suspend fun addToFavorite(id: String) {
        photoDao.insert(PhotoDbEntity(id))
    }

    override suspend fun removeFromFavorite(id: String) {
        photoDao.remove(id)
    }
}