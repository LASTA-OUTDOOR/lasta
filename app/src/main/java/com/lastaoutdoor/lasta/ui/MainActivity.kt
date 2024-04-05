package com.lastaoutdoor.lasta.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.view.MainMenu
import com.lastaoutdoor.lasta.view.screen.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {

      // The navcontroller that allows to go from the login screen to the main menu screen
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = "login") {
        composable("mainMenu") { MainMenu() } // The map is the main screen for now
        composable("login") {
          LoginScreen(onNavigateToMain = { navController.navigate("mainMenu") })
        }
      }
    }
  }
}
