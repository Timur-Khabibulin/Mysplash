package com.timurkhabibulin.mysplash.navigation


import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.home.HomeApi
import com.timurkhabibulin.search.SearchApi
import com.timurkhabibulin.topics.topics.TopicsApi
import com.timurkhabibulin.user.favorites.FavoriteApi


val BOTTOM_BAR_ITEMS = setOf<FeatureApi>(
    HomeApi,
    TopicsApi,
    SearchApi,
    FavoriteApi
)