package com.lastaoutdoor.lasta.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lastaoutdoor.lasta.ui.screen.loading.LoadingScreen
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

fun NavGraphBuilder.addLoadingNavGraph(navController: NavHostController) {
  navigation(startDestination = DestinationRoute.Loading.route, route = BaseRoute.Loading.route) {
    composable(DestinationRoute.Loading.route) { entry ->
      val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)
      LoadingScreen(
          isLoggedIn = preferencesViewModel.isLoggedIn.value,
          navigateWhenLoggedIn = {
            navController.popBackStack()
            navController.navigate(BaseRoute.Main.route)
          },
          navigateWhenLoggedOut = {
            navController.popBackStack()
            navController.navigate(BaseRoute.Login.route)
          })
    }
  }
}
