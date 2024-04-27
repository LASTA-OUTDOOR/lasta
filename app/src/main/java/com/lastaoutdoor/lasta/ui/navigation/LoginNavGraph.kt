package com.lastaoutdoor.lasta.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lastaoutdoor.lasta.ui.screen.login.LoginScreen
import com.lastaoutdoor.lasta.ui.screen.setup.SetupScreen
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel

fun NavGraphBuilder.addLoginNavGraph(navController: NavHostController) {
  navigation(startDestination = DestinationRoute.SignIn.route, route = BaseRoute.Login.route) {
    composable(DestinationRoute.SignIn.route) { entry ->
      val authViewModel: AuthViewModel = entry.sharedViewModel(navController)
      LoginScreen()
    }
    composable(DestinationRoute.Setup.route) { SetupScreen() }
  }
}
