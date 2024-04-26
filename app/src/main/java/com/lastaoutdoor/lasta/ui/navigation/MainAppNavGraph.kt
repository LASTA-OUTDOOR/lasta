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
      route = BaseRoute.Root.route,
      startDestination = BaseRoute.Loading.route) {
        composable(BaseRoute.Loading.route) { LoadingScreen(navController = navController) }
        composable(BaseRoute.Login.route) { LoginNavGraph(rootNavController = navController) }
        composable(BaseRoute.Main.route) { MainScreen(rootNavController = navController) }
      }
}
