package com.lastaoutdoor.lasta.ui.screen.login.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.utils.Response.Failure
import com.lastaoutdoor.lasta.utils.Response.Loading
import com.lastaoutdoor.lasta.utils.Response.Success
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

@Composable
fun FinishGoogleSignInEffect(
    authViewModel: AuthViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit
) {
  when (val signInWithGoogleResponse = authViewModel.signInWithGoogleResponse) {
    is Loading -> Unit
    is Success ->
        signInWithGoogleResponse.data?.let { user ->
          LaunchedEffect(user) {
            preferencesViewModel.updateIsLoggedIn(true)
            preferencesViewModel.updateUserInfo(user)
            navigateToHomeScreen()
          }
        }
    is Failure -> LaunchedEffect(Unit) { print(signInWithGoogleResponse.e) }
  }
}
