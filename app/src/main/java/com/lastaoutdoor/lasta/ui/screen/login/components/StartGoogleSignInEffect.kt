package com.lastaoutdoor.lasta.ui.screen.login.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.lastaoutdoor.lasta.utils.Response.Failure
import com.lastaoutdoor.lasta.utils.Response.Loading
import com.lastaoutdoor.lasta.utils.Response.Success
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel

@Composable
fun StartGoogleSignInEffect(
    viewModel: AuthViewModel = hiltViewModel(),
    launch: (result: BeginSignInResult) -> Unit
) {
  when (val oneTapSignInResponse = viewModel.oneTapSignInResponse) {
    is Loading -> Unit
    is Success -> oneTapSignInResponse.data?.let { LaunchedEffect(it) { launch(it) } }
    is Failure -> LaunchedEffect(Unit) { print(oneTapSignInResponse.e) }
  }
}
