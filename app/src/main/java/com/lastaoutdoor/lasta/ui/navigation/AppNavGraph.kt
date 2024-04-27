package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

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
            route = BaseRoute.Root.route,
            startDestination = BaseRoute.Loading.route) {
              addLoginNavGraph(navigationState.navController)
            }
      }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
  val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
  val parentEntry = remember(this) { navController.getBackStackEntry(navGraphRoute) }
  return hiltViewModel(parentEntry)
}
