package com.timurkhabibulin.core.analytics

enum class ContentType {
    BUTTON {
        override fun toString() = "button"
    },
    IMAGE_BUTTON {
        override fun toString() = "image_button"
    }
}