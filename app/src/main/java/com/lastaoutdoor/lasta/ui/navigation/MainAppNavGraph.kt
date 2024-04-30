package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lastaoutdoor.lasta.ui.screen.loading.LoadingScreen
import com.lastaoutdoor.lasta.ui.screen.main.MainScreen

@Composable
fun MainAppNavGraph(
    navController: NavHostController,
) {
  NavHost(
      navController = navController,
      modifier = Modifier.testTag("MainAppNavGraph"),
      route = RootScreen.Root.route,
      startDestination = RootScreen.Loading.route) {
        composable(RootScreen.Loading.route) { LoadingScreen(navController = navController) }
        composable(RootScreen.Login.route) { LoginNavGraph(rootNavController = navController) }
        composable(RootScreen.Main.route) { MainScreen(rootNavController = navController) }
      }
}