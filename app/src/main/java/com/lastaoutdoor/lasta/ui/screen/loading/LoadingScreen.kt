package com.lastaoutdoor.lasta.ui.screen.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

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

  Box(
      modifier = Modifier.fillMaxSize().testTag("LoadingScreen"),
      contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.width(100.dp))
      }
}
