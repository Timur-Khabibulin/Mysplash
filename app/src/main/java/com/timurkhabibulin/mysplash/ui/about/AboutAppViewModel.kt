package com.timurkhabibulin.mysplash.ui.about

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutAppViewModel @Inject constructor() : ViewModel() {
    fun openGitHub(context: Context) {
        openDeepLink(context, "https://github.com/Timur-Khabibulin/Mysplash")
    }

    fun openDeveloper(context: Context) {
        openDeepLink(context, "https://unsplash.com/@timurkh")
    }

    private fun openDeepLink(context: Context, link: String) {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            link.toUri()
        )
        context.startActivity(deepLinkIntent)
    }
}