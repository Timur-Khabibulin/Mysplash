package com.timurkhabibulin.core.analytics

interface Analytics {
    fun logScreenView(name: String)
    fun logEvent(analyticsEvent: AnalyticsEvent)
}