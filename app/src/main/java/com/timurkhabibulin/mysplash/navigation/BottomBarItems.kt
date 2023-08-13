package com.timurkhabibulin.mysplash.navigation


import com.timurkhabibulin.core.FeatureApi
import com.timurkhabibulin.home.HomeApi
import com.timurkhabibulin.search.SearchApi
import com.timurkhabibulin.topics.topics.TopicsApi


val BOTTOM_BAR_ITEMS = setOf<FeatureApi>(
    HomeApi,
    TopicsApi,
    SearchApi
)