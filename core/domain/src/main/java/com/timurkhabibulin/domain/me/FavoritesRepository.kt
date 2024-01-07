package com.timurkhabibulin.domain.me

interface FavoritesRepository {
    suspend fun getFavoritePhotos(limit: Int, offset: Int): List<String>

    suspend fun isFavorite(id: String): Boolean

    suspend fun addToFavorite(id: String)

    suspend fun removeFromFavorite(id: String)
}