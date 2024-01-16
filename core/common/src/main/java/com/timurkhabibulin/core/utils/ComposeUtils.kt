package com.timurkhabibulin.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.staticCompositionLocalOf
import com.timurkhabibulin.core.analytics.Analytics
import com.timurkhabibulin.core.analytics.AnalyticsEvent

val LocalAnalytics = staticCompositionLocalOf<Analytics> {
    object : Analytics {
        override fun logScreenView(name: String) {}
        override fun logEvent(analyticsEvent: AnalyticsEvent) {}
    }
}

@Composable
fun TrackScreen(name: String) {
    val analytics = LocalAnalytics.current
    DisposableEffect(Unit) {
        analytics.logScreenView(name)
        onDispose { }
    }
}