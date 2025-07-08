package com.deelib.perfScout.sample.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = androidx.compose.ui.graphics.Color(0xFF1EB980),
    primaryVariant = androidx.compose.ui.graphics.Color(0xFF045D56),
    secondary = androidx.compose.ui.graphics.Color(0xFF546E7A)
)

private val LightColorPalette = lightColors(
    primary = androidx.compose.ui.graphics.Color(0xFF1EB980),
    primaryVariant = androidx.compose.ui.graphics.Color(0xFF045D56),
    secondary = androidx.compose.ui.graphics.Color(0xFF546E7A)
)

@Composable
fun PerfScoutTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
} 