package com.lastaoutdoor.lasta.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.navigation.RootScreen
import com.lastaoutdoor.lasta.ui.screen.LoginScreen
import com.lastaoutdoor.lasta.ui.screen.MainScreen
import com.lastaoutdoor.lasta.ui.theme.LastaTheme
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val authViewModel by viewModels<AuthViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      // The navcontroller that allows to go from the login screen to the main menu screen
      val navController = rememberNavController()
      LastaTheme {
        NavHost(
            navController = navController,
            route = RootScreen.Root.route,
            startDestination = RootScreen.Login.route) {
              composable(RootScreen.Login.route) {
                LaunchedEffect(key1 = Unit) { authViewModel.fetchAuthInfo() }
                LoginScreen(
                    authViewModel,
                    onLogin = {
                      navController.popBackStack()
                      navController.navigate(RootScreen.Main.route)
                    })
              }
              composable(RootScreen.Main.route) {
                MainScreen(
                    onSignOut = {
                      authViewModel.signOut()
                      navController.popBackStack()
                      navController.navigate(RootScreen.Login.route)
                    })
              }
            }
      }
    }
  }
}
