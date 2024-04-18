package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.ui.screen.login.LoginScreen
import com.lastaoutdoor.lasta.ui.screen.setup.SetupScreen

@Composable
fun LoginNavGraph(
    rootNavController: NavHostController,
) {
  val navController = rememberNavController()
  NavHost(
      navController = navController,
      route = RootScreen.Login.route,
      startDestination = LeafScreen.SignIn.route) {
        composable(LeafScreen.SignIn.route) { LoginScreen(navController = navController) }
        composable(LeafScreen.Setup.route) { SetupScreen(rootNavController = rootNavController) }
      }
}
