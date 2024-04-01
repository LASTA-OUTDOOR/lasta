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
import com.lastaoutdoor.lasta.ui.screen.LoginScreen
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

      NavHost(navController = navController, startDestination = "login") {
        composable("mainMenu") { MainMenu(authViewModel, navController) } // The map is the main screen for now
        composable("login") {
          LaunchedEffect(key1 = Unit) { authViewModel.getCurrentUser() }
          LoginScreen(authViewModel, onNavigateToMain = { navController.navigate("mainMenu") })
        }
      }
    }
  }
}
