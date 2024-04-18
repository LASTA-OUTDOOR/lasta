package com.lastaoutdoor.lasta.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// TODO: Make sure the color schemes match the wanted design
private val DarkColorScheme =
    darkColorScheme(
        primary = Black,  // This is the color for backgrounds and surfaces
        secondary = White, // This is the color for text and icons
        tertiary = LastaBlue, // This is the color for primary actions
        //background = LastaBlue,
        //surface = LastaBlue,
        onPrimary = MarkerGreen, // This is the color for unselected markers
        onSecondary = MarkerBlue, // This is the color for selected markers
        //onTertiary = Color.White,
        //onBackground = LastaBlue,
        //onSurface = LastaBlue,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = White, // This is the color for backgrounds and surfaces
        secondary = Black, // This is the color for text and icons
        tertiary = LastaBlue, // This is the color for primary actions
        background = LastaBlue,
        surface = LastaBlue,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = LastaBlue,
        onSurface = LastaBlue,
        )

@Composable
fun LastaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
  val colorScheme =
      when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
      }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
