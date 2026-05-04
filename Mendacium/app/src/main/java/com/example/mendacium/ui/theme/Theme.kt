package com.example.mendacium.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MendaciumDarkColorScheme = darkColorScheme(
    primary = PurpleAccent,
    secondary = GreyLavender,
    tertiary = ImpostorRed,
    background = DarkBackground,
    surface = CardBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val MendaciumLightColorScheme = lightColorScheme(
    primary = PurpleAccent,
    secondary = GreyLavender,
    tertiary = ImpostorRed,
    background = Color(0xFFF4F1FA),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DarkBackground,
    onSurface = DarkBackground
)

@Composable
fun MendaciumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        MendaciumDarkColorScheme
    } else {
        MendaciumLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
