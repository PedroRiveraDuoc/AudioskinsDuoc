package com.example.audioskinsduoc.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define los colores personalizados aquí
private val LightColorScheme = lightColorScheme(
    primary = MyPrimary,
    onPrimary = White,
    secondary = MySecondary,
    onSecondary = White,
    background = DodgerBlue300, // Color de fondo
    onBackground = Black,
    surface = White,
    onSurface = Black,
)

private val DarkColorScheme = darkColorScheme(
    primary = MyPrimary,
    onPrimary = White,
    secondary = MySecondary,
    onSecondary = White,
    background = DodgerBlue300, // Color de fondo para modo oscuro
    onBackground = White,
    surface = DodgerBlue800,
    onSurface = White,
)

@Composable
fun AudioskinsDuocTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
