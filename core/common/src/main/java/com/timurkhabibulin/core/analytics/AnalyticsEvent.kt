package com.timurkhabibulin.core.analytics

data class AnalyticsEvent(
    val action: AnalyticsAction,
    val contentType: ContentType,
    val screenName: String? = null
)