package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation

@Composable
fun AppNavGraph() {
  val navigationState = rememberNavigationState()
  Scaffold(
      bottomBar = {
        if (navigationState.shouldShowBottomBar)
            BottomBar(
                tabs = navigationState.bottomBarTabs,
                currentRoute = navigationState.currentRoute!!,
                navigateToRoute = navigationState::navigateToBottomBarRoute)
      }) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navigationState.navController,
            startDestination = "start") {
              navigation(startDestination = "start", route = "start") {}
            }
      }
}
