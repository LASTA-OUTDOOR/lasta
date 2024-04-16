package com.lastaoutdoor.lasta.ui.screen.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.ui.navigation.RootScreen
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    navController: NavHostController,
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
  val isLoggedIn by preferencesViewModel.isLoggedIn.collectAsState(initial = false)
  LaunchedEffect(key1 = preferencesViewModel.isLoggedIn) {
    delay(400)
    navController.popBackStack()
    if (isLoggedIn) {
      navController.navigate(RootScreen.Main.route)
    } else {
      navController.navigate(RootScreen.Login.route)
    }
  }

  Box(
      modifier = Modifier.fillMaxSize().testTag("LoadingScreen"),
      contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.width(100.dp))
      }
}
