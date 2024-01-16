package com.timurkhabibulin.mysplash.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.timurkhabibulin.core.analytics.Analytics
import com.timurkhabibulin.core.analytics.AnalyticsEvent
import javax.inject.Inject

class AnalyticsImpl @Inject constructor() : Analytics {
    private val firebaseAnalytics = Firebase.analytics
    override fun logScreenView(name: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, name)
        }
    }

    override fun logEvent(analyticsEvent: AnalyticsEvent) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_NAME, analyticsEvent.action.toString())
            param(FirebaseAnalytics.Param.CONTENT_TYPE, analyticsEvent.contentType.toString())
            analyticsEvent.screenName?.let {
                param(FirebaseAnalytics.Param.SCREEN_NAME, it)
            }
        }
    }
}