package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lastaoutdoor.lasta.ui.screen.loading.LoadingScreen
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

fun NavGraphBuilder.addLoadingNavGraph(navController: NavHostController) {
  navigation(startDestination = DestinationRoute.Loading.route, route = BaseRoute.Loading.route) {
    composable(DestinationRoute.Loading.route) { entry ->
      val preferencesViewModel: PreferencesViewModel = hiltViewModel(entry)
      LoadingScreen(
          isLoggedIn = preferencesViewModel.isLoggedIn.observeAsState(initial = null).value,
          navigateWhenLoggedIn = {
            navController.navigate(BaseRoute.Main.route) {
              popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
          },
          navigateWhenLoggedOut = {
            navController.navigate(BaseRoute.Login.route) {
              popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
          })
    }
  }
}
