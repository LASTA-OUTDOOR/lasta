package com.lastaoutdoor.lasta.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.ui.navigation.MainAppNavGraph
import com.lastaoutdoor.lasta.ui.theme.LastaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      // The navcontroller that allows to go from the login screen to the main menu screen
      val navController = rememberNavController()

      LastaTheme { MainAppNavGraph(navController = navController) }
    }
  }
}
