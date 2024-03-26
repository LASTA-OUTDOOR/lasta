package com.lastaoutdoor.lasta.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.view.screen.LoginScreen
import com.lastaoutdoor.lasta.view.screen.MapScreen
import com.lastaoutdoor.lasta.view.theme.LastaTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {

      //The navcontroller that allows to go from the login screen to the main menu screen
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = "login") {
        composable("mainMenu") { MainMenu() }
        composable("login") { LoginScreen(onNavigateToMain = { navController.navigate("mainMenu") }) }
      }

    }
  }

  @Composable
  fun MainMenu() {
    Text(text = "main menu")
  }

  data class Task(val name: String, val description: String)

  @Composable
  fun MapTab() {
    MapScreen();
  }

}
