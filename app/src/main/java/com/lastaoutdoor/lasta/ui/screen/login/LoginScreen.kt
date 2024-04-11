package com.lastaoutdoor.lasta.ui.screen.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.lastaoutdoor.lasta.ui.navigation.RootScreen
import com.lastaoutdoor.lasta.ui.screen.login.components.LoginContent
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {

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

  LaunchedEffect(key1 = authViewModel.beginSignInResult) {
    authViewModel.beginSignInResult?.let {
      val intent = IntentSenderRequest.Builder(it.pendingIntent.intentSender).build()
      launcher.launch(intent)
    }
  }

  LaunchedEffect(key1 = authViewModel.user) {
    authViewModel.user?.let {
      preferencesViewModel.updateIsLoggedIn(true)
      preferencesViewModel.updateUserInfo(it)
      navController.popBackStack()
      navController.navigate(RootScreen.Main.route)
    }
  }

  LoginContent(onLoginClick = { authViewModel.startGoogleSignIn() })
}
