package com.lastaoutdoor.lasta.ui.screen.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.lastaoutdoor.lasta.ui.navigation.RootScreen
import com.lastaoutdoor.lasta.ui.screen.login.components.FinishGoogleSignInEffect
import com.lastaoutdoor.lasta.ui.screen.login.components.LoginContent
import com.lastaoutdoor.lasta.ui.screen.login.components.StartGoogleSignInEffect
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel()) {

  val launcher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
          result ->
        if (result.resultCode == Activity.RESULT_OK) {
          try {
            val credentials = authViewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
            val googleIdToken = credentials.googleIdToken
            val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
            authViewModel.finishGoogleSignIn(googleCredentials)
          } catch (it: ApiException) {
            print(it)
          }
        }
      }

  fun launch(signInResult: BeginSignInResult) {
    val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
    launcher.launch(intent)
  }

  LoginContent(onLoginClick = { authViewModel.startGoogleSignIn() })

  StartGoogleSignInEffect(launch = { launch(it) })

  FinishGoogleSignInEffect(
      navigateToHomeScreen = {
        navController.popBackStack()
        navController.navigate(RootScreen.Main.route)
      })
}
