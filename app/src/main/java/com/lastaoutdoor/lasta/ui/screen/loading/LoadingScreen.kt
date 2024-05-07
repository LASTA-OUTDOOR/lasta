package com.lastaoutdoor.lasta.ui.screen.loading

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.lastaoutdoor.lasta.ui.components.LoadingAnim

@Composable
fun LoadingScreen(
    isLoggedIn: Boolean?,
    navigateWhenLoggedIn: () -> Unit,
    navigateWhenLoggedOut: () -> Unit
) {
  LaunchedEffect(key1 = isLoggedIn) {
    if (isLoggedIn != null) {
      if (isLoggedIn == true) {
        navigateWhenLoggedIn()
      } else {
        navigateWhenLoggedOut()
      }
    }
  }

  LoadingAnim(width = 100, tag = "LoadingScreen")
}
