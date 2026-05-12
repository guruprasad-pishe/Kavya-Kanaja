package com.kavyakanaja.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SaffronLight,
    onPrimary = InkBrown,
    primaryContainer = SaffronDeep,
    onPrimaryContainer = GoldLight,
    secondary = GoldAccent,
    onSecondary = InkBrown,
    background = NightBlue,
    onBackground = ParchmentLight,
    surface = NightCard,
    onSurface = ParchmentLight,
    surfaceVariant = NightSurface,
    outline = GoldAccent
)

private val LightColorScheme = lightColorScheme(
    primary = SaffronDeep,
    onPrimary = ParchmentLight,
    primaryContainer = GoldLight,
    onPrimaryContainer = InkBrown,
    secondary = GoldAccent,
    onSecondary = InkBrown,
    background = ParchmentLight,
    onBackground = InkBrown,
    surface = ParchmentDark,
    onSurface = InkBrown,
    surfaceVariant = Color(0xFFF0E0C0),
    outline = SaffronDeep
)

// Fix: import Color properly
private fun Color(value: Long) = androidx.compose.ui.graphics.Color(value)

@Composable
fun KavyaKanajaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
