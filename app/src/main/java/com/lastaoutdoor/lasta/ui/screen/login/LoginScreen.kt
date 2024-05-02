package com.lastaoutdoor.lasta.ui.screen.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.screen.login.components.LoginContent

@Composable
fun LoginScreen(
    beginSignInResult: BeginSignInResult?,
    user: UserModel?,
    isSignUp: Boolean,
    startGoogleSignIn: () -> Unit,
    finishGoogleSignIn: (AuthCredential) -> Unit,
    oneTapClient: SignInClient,
    updatePreferencesOnLogin: (UserModel) -> Unit,
    navigateToSetup: () -> Unit,
    navigateToMain: () -> Unit
) {
  val launcher =
      rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
          result ->
        if (result.resultCode == Activity.RESULT_OK) {
          try {
            val credentials = oneTapClient.getSignInCredentialFromIntent(result.data)
            val googleIdToken = credentials.googleIdToken
            val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
            finishGoogleSignIn(googleCredentials)
          } catch (it: ApiException) {
            print(it)
          }
        }
      }

  LaunchedEffect(key1 = beginSignInResult) {
    beginSignInResult?.let {
      val intent = IntentSenderRequest.Builder(it.pendingIntent.intentSender).build()
      launcher.launch(intent)
    }
  }

  LaunchedEffect(key1 = authViewModel.user) {
    authViewModel.user?.let {
      updatePreferencesOnLogin(it)
      if (isSignUp) {
        navigateToSetup()
      } else {
        navigateToMain()
      }
    }
  }
  Spacer(modifier = Modifier.testTag("loginScreenMain"))

  LoginContent(onLoginClick = { startGoogleSignIn() })
}
