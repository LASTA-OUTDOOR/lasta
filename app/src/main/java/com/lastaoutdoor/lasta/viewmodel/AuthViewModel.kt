package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.repository.OneTapSignInResponse
import com.lastaoutdoor.lasta.repository.SignInWithGoogleResponse
import com.lastaoutdoor.lasta.repository.SignOutResponse
import com.lastaoutdoor.lasta.utils.Response.Loading
import com.lastaoutdoor.lasta.utils.Response.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel
@Inject
constructor(private val authRepo: AuthRepository, val oneTapClient: SignInClient) : ViewModel() {
  val currentUser
    get() = authRepo.currentUser

  var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse>(Success(null))
  var signInWithGoogleResponse by mutableStateOf<SignInWithGoogleResponse>(Success(null))

  var signOutResponse by mutableStateOf<SignOutResponse>(Success(false))

  fun startGoogleSignIn() =
      viewModelScope.launch {
        oneTapSignInResponse = Loading
        oneTapSignInResponse = authRepo.startGoogleSignIn()
      }

  fun finishGoogleSignIn(googleCredential: AuthCredential) =
      viewModelScope.launch {
        signInWithGoogleResponse = Loading
        signInWithGoogleResponse = authRepo.finishGoogleSignIn(googleCredential)
      }

  fun signOut() =
      viewModelScope.launch {
        signOutResponse = Loading
        signOutResponse = authRepo.signOut()
      }
}
