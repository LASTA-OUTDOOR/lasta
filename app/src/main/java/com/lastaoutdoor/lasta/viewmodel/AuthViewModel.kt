package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.utils.Response
import com.lastaoutdoor.lasta.utils.Response.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel
@Inject
constructor(private val authRepo: AuthRepository, val oneTapClient: SignInClient) : ViewModel() {

  var beginSignInResult: BeginSignInResult? by mutableStateOf(null)
  var user: UserModel? by mutableStateOf(null)
  var signedOut: Boolean by mutableStateOf(false)

  fun startGoogleSignIn(isSignUp: Boolean) {
    viewModelScope.launch {
      authRepo.startGoogleSignIn(isSignUp).collect { response ->
        when (response) {
          is Response.Loading -> {}
          is Success -> {
            beginSignInResult = response.data
          }
          is Response.Failure -> {
            response.e.printStackTrace()
            throw response.e
          }
        }
      }
    }
  }

  fun finishGoogleSignIn(googleCredential: AuthCredential, isSignUp: Boolean) {
    viewModelScope.launch {
      authRepo.finishGoogleSignIn(googleCredential, isSignUp).collect { response ->
        when (response) {
          is Response.Loading -> {}
          is Success -> {
            user = response.data
          }
          is Response.Failure -> {
            response.e.printStackTrace()
            throw response.e
          }
        }
      }
    }
  }

  fun signOut() {
    viewModelScope.launch {
      authRepo.signOut().collect { response ->
        when (response) {
          is Response.Loading -> {}
          is Success -> {
            signedOut = true
          }
          is Response.Failure -> {
            response.e.printStackTrace()
            throw response.e
          }
        }
      }
    }
  }
}
