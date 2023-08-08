package com.timurkhabibulin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val appColorScheme = darkColorScheme(
    primary = app_primary,
    primaryContainer = app_primaryContainer,
    secondary = app_secondary,
    secondaryContainer = app_secondaryContainer,
    background = app_background,
    surface = app_surface
)

@Composable
fun MysplashTheme(
    /*    darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        dynamicColor: Boolean = true,*/
    content: @Composable () -> Unit
) {
    /*    val colorScheme = when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            else -> darkColors
    //        else -> LightColors
        }
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
            }
        }*/

    MaterialTheme(
        colorScheme = appColorScheme,
        typography = appTypography,
        content = content
    )
}