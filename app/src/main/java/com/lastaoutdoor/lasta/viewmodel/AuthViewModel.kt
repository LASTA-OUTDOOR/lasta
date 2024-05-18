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
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
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
    val oneTapClient: SignInClient,
    private val errorToast: ErrorToast
) : ViewModel() {

  var beginSignInResult: BeginSignInResult? by mutableStateOf(null)
  var user: UserModel? by mutableStateOf(null)
  var signedOut: Boolean by mutableStateOf(false)
  val isSignUp = authRepo.isSignUp

  fun startGoogleSignIn() {
    viewModelScope.launch {
      try {
        authRepo.startGoogleSignIn().collect { response ->
          when (response) {
            is Response.Loading -> {}
            is Response.Success -> {
              beginSignInResult = response.data
            }
            is Response.Failure -> {
              errorToast.showToast(ErrorType.ERROR_SIGN_IN)
            }
          }
        }
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_SIGN_IN)
      }
    }
  }

  fun finishGoogleSignIn(googleCredential: AuthCredential) {
    viewModelScope.launch {
      try {
        authRepo.finishGoogleSignIn(googleCredential).collect { response ->
          when (response) {
            is Response.Loading -> {}
            is Response.Success -> {
              user = response.data
            }
            is Response.Failure -> {
              errorToast.showToast(ErrorType.ERROR_SIGN_IN)
            }
          }
        }
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_SIGN_IN)
      }
    }
  }

  fun signOut() {
    viewModelScope.launch {
      try {
        authRepo.signOut().collect { response ->
          when (response) {
            is Response.Loading -> {}
            is Response.Success -> {
              user = null
              beginSignInResult = null
              signedOut = true
            }
            is Response.Failure -> {
              errorToast.showToast(ErrorType.ERROR_SIGN_OUT)
            }
          }
        }
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_SIGN_OUT)
      }
    }
  }


  fun updateFieldInUser(userId: String, field: String, value: Any) {
    viewModelScope.launch {
      try {
        userDBRepository.updateField(userId, field, value)
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DATABASE)
      }
    }
  }
}
