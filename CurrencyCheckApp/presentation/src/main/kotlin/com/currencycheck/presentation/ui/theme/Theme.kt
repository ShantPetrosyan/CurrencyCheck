package com.currencycheck.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorPalette = darkColorScheme(
    primary = PrimaryColor,
    primaryContainer = OnPrimaryColor,
    secondary = SecondaryColor,

    background = DefaultBackgroundColor,
    surface = DefaultBackgroundColor,
    onSecondary = TextDefaultColor
)

private val LightColorPalette = lightColorScheme(
    primary = PrimaryColor,
    primaryContainer = OnPrimaryColor,
    secondary = SecondaryColor,
    background = DefaultBackgroundColor,
    surface = DefaultBackgroundColor,
    onSecondary = TextDefaultColor,
    secondaryContainer = DefaultBackgroundColor
)

@Composable
fun CurrencyCheckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // set to false
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor =
                if (darkTheme) TextSecondaryColor.toArgb() else HeaderColor.toArgb() // change color status bar here
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}