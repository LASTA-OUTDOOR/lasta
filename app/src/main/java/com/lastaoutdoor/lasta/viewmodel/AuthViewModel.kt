package com.lastaoutdoor.lasta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.auth.AuthRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val authRepo: AuthRepository,
    private val userDBRepository: UserDBRepository,
    val oneTapClient: SignInClient
) : ViewModel() {

  var beginSignInResult: BeginSignInResult? by mutableStateOf(null)
  var user: UserModel? by mutableStateOf(null)
  var signedOut: Boolean by mutableStateOf(false)
  val isSignUp = authRepo.isSignUp

  fun startGoogleSignIn() {
    viewModelScope.launch {
      authRepo.startGoogleSignIn().collect { response ->
        when (response) {
          is Response.Loading -> {}
          is Response.Success -> {
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

  fun finishGoogleSignIn(googleCredential: AuthCredential) {
    viewModelScope.launch {
      authRepo.finishGoogleSignIn(googleCredential).collect { response ->
        when (response) {
          is Response.Loading -> {}
          is Response.Success -> {
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
          is Response.Success -> {
            user = null
            beginSignInResult = null
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

  fun updateFieldInUser(userId: String, field: String, value: Any) {
    viewModelScope.launch { userDBRepository.updateField(userId, field, value) }
  }
}
