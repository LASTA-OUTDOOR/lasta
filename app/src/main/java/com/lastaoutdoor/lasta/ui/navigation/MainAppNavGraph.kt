package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lastaoutdoor.lasta.ui.screen.loading.LoadingScreen
import com.lastaoutdoor.lasta.ui.screen.login.LoginScreen
import com.lastaoutdoor.lasta.ui.screen.main.MainScreen
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

@Composable
fun MainAppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
  NavHost(
      navController = navController,
      route = RootScreen.Root.route,
      startDestination = RootScreen.Loading.route) {
        composable(RootScreen.Loading.route) { LoadingScreen(navController = navController) }
        composable(RootScreen.Login.route) { LoginScreen(navController = navController) }
        composable(RootScreen.Main.route) { MainScreen(rootNavController = navController) }
      }
}
