package com.lastaoutdoor.lasta.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// TODO: Make sure the color schemes match the wanted design
private val DarkColorScheme =
    darkColorScheme(
        primary = LastaBlue,  // This is the color for backgrounds and surfaces
        secondary = LastaGreen, // This is the color for text and icons
        onPrimary = White, // This is the color for text and icons on top of primary
        background = DarkBackground, // This is the color for the background
        surfaceVariant = DarkSurfaceVariant, // This is the color for card surfaces
        onSurfaceVariant = White, // This is the color for text and icons on top of surfaceVariant
        onBackground = White, // This is the color for text and icons on top of background
        primaryContainer = LastaGreen, // This is the color for primary containers
        secondaryContainer = SecondaryContainerColor, // This is the color for secondary containers
    )

private val LightColorScheme =
    lightColorScheme(
        primary = LastaBlue, // This is the color for backgrounds and surfaces
        secondary = LastaGreen, // This is the color for text and icons
        surfaceVariant = SurfaceVariant, // This is the color for card surfaces
        onPrimary = White, // This is the color for text and icons on top of primary
        primaryContainer = LastaGreen, // This is the color for primary containers
        secondaryContainer = SecondaryContainerColor, // This is the color for secondary containers
        )

@Composable
fun LastaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // choose color scheme based on darkTheme
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
