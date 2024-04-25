package com.lastaoutdoor.lasta.ui.screen.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.ui.navigation.RootScreen
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

@Composable
fun LoadingScreen(
    navController: NavHostController,
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
  val isLoggedIn by preferencesViewModel.isLoggedIn.observeAsState()
  LaunchedEffect(key1 = isLoggedIn) {
    if (isLoggedIn != null) {
      navController.popBackStack()
      if (isLoggedIn == true) {
        navController.navigate(RootScreen.Main.route)
      } else {
        navController.navigate(RootScreen.Login.route)
      }
    }
  }

  Box(
      modifier = Modifier.fillMaxSize().testTag("LoadingScreen"),
      contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.width(100.dp))
      }
}
