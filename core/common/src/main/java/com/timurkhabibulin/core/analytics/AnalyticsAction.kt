package com.timurkhabibulin.core.analytics

enum class AnalyticsAction {
    DOWNLOAD {
        override fun toString() = "download"
    },
    SET_WALLPAPER {
        override fun toString() = "set_wallpaper"
    },
    OPEN_IN_BROWSER {
        override fun toString() = "open_in_browser"
    },
    ADD_TO_FAVORITE {
        override fun toString() = "add_to_favorite"
    },
    SEARCH {
        override fun toString() = "search"
    }
}