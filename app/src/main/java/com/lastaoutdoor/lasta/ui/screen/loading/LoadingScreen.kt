package com.lastaoutdoor.lasta.ui.screen.loading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.messaging.FirebaseMessaging
import com.lastaoutdoor.lasta.ui.components.LoadingAnim
import kotlinx.coroutines.tasks.await

@Composable
fun LoadingScreen(
    isLoggedIn: Boolean?,
    navigateWhenLoggedIn: () -> Unit,
    navigateWhenLoggedOut: () -> Unit
) {
  LaunchedEffect(key1 = isLoggedIn) {
      //print token fcm
        println(FirebaseMessaging.getInstance().token.await())
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
