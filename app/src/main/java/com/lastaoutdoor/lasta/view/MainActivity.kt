package com.lastaoutdoor.lasta.view

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
import com.lastaoutdoor.lasta.view.theme.LastaTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { GlobalNav() }
  }

  private fun onSignInResult(
      result: FirebaseAuthUIAuthenticationResult,
      onNavigateToMain: () -> Unit
  ) {
    val response = result.idpResponse
    if (result.resultCode == RESULT_OK) {
      // Successfully signed in
      val user = FirebaseAuth.getInstance().currentUser
      onNavigateToMain()
    }
  }

  @Composable
  fun GlobalNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
      composable("mainMenu") { MainMenu() }
      composable("login") { LoginMenu(onNavigateToMain = { navController.navigate("mainMenu") }) }
    }
  }

  @Composable
  fun MainMenu() {
    Text(text = "main menu")
  }

  data class Task(val name: String, val description: String)

  @Composable
  fun MapTab() {
    Text(text = "map tab")
  }

  @Composable
  fun LoginMenu(onNavigateToMain: () -> Unit) {
    val providers =
        arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )
    // Create and launch sign-in intent
    val signInIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()
    val signInLauncher =
        rememberLauncherForActivityResult(
            FirebaseAuthUIActivityResultContract(),
        ) { r ->
          this.onSignInResult(r, onNavigateToMain)
        }
    LastaTheme {
      Column(
          modifier = Modifier.fillMaxSize().padding(15.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(189.dp))
            Spacer(modifier = Modifier.size(35.dp))
            Text(
                text = "LASTA",
                modifier = Modifier.width(256.dp).height(65.dp),
                style =
                    TextStyle(
                        fontSize = 57.sp,
                        lineHeight = 64.sp,
                        color = Color(0xFF191C1E),
                        textAlign = TextAlign.Center,
                    ))
            Spacer(modifier = Modifier.size(150.dp))
            Button(
                onClick = { signInLauncher.launch(signInIntent) },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.Gray),
                content = {
                  Image(
                      painter = painterResource(id = R.drawable.google_logo),
                      contentDescription = "Google Logo",
                      modifier = Modifier.size(24.dp))
                  Text(
                      text = "Sign in with Google",
                      modifier = Modifier.padding(6.dp),
                      color = Color.Black,
                      fontSize = 14.sp)
                })
          }
    }
  }
}
