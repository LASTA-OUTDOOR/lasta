package com.lastaoutdoor.lasta.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// TODO: Make sure the color schemes match the wanted design
private val DarkColorScheme =
    darkColorScheme(
        primary = PrimaryBlue, // This is the color for backgrounds and surfaces
        secondary = AccentGreen, // This is the color for text and icons
        onPrimary = White, // This is the color for text and icons on top of primary
        onSecondary = White, // This is the color for text and icons on top of secondary
        background = DarkBackground, // This is the color for the background
        surfaceVariant = DarkSurfaceVariant, // This is the color for card surfaces
        onSurfaceVariant = White, // This is the color for text and icons on top of surfaceVariant
        onBackground = White, // This is the color for text and icons on top of background
        primaryContainer = AccentGreen, // This is the color for primary containers
        secondaryContainer = SecondaryContainerColor, // This is the color for secondary containers
        onSecondaryContainer =
            VeryLightBlue, // This is the color for text and icons on top of secondaryContainer
        onSurface =
            VeryLightBlue // This is the color for text and icons on top of secondaryContainer
        )

private val LightColorScheme =
    lightColorScheme(
        primary = PrimaryBlue, // This is the color for backgrounds and surfaces
        secondary = AccentGreen, // This is the color for text and icons
        onPrimary = White, // This is the color for text and icons on top of primary
        onSecondary = White, // This is the color for text and icons on top of secondary
        surfaceVariant = SurfaceVariant, // This is the color for card surfaces
        primaryContainer = AccentGreen, // This is the color for primary containers
        secondaryContainer = SecondaryContainerColor, // This is the color for secondary containers
        onBackground = Black, // This is the color for text and icons on top of background
        onSurfaceVariant = Black, // This is the color for text and icons on top of surfaceVariant
        onSecondaryContainer =
            PrimaryBlue, // This is the color for text and icons on top of secondaryContainer
        onSurface = PrimaryBlue // This is the color for text and icons on top of secondaryContainer
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
