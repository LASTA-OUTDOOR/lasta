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
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.repository.auth.AuthRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import com.lastaoutdoor.lasta.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    private val authRepo: AuthRepository,
    private val userDBRepository: UserDBRepository,
    val oneTapClient: SignInClient,
    private val errorToast: ErrorToast,
    private val connectivityRepositoryImpl: ConnectivityRepository,
) : ViewModel() {
  val isConnected =
      connectivityRepositoryImpl.connectionState.stateIn(
          initialValue = ConnectionState.OFFLINE,
          scope = viewModelScope,
          started = SharingStarted.WhileSubscribed(5000))
  var beginSignInResult: BeginSignInResult? by mutableStateOf(null)
  var user: UserModel? by mutableStateOf(null)
  var signedOut: Boolean by mutableStateOf(false)
  val isSignUp = authRepo.isSignUp

  fun startGoogleSignIn() {
    viewModelScope.launch {

      // Call surrounded by try-catch block to make handle exceptions for sign in
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
        e.printStackTrace()
      }
    }
  }

  fun finishGoogleSignIn(googleCredential: AuthCredential) {
    viewModelScope.launch {
      // Call surrounded by try-catch block to make handle exceptions for sign in
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
        e.printStackTrace()
      }
    }
  }

  fun signOut() {
    viewModelScope.launch {

      // Call surrounded by try-catch block to make handle exceptions for sign out
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
        e.printStackTrace()
      }
    }
  }

  fun deleteAccount() {
    viewModelScope.launch {

      // Call surrounded by try-catch block to make handle exceptions for account deletion
      try {
        authRepo.deleteAccount().collect { response ->
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
        e.printStackTrace()
      }
    }
  }

  fun updateFieldInUser(userId: String, field: String, value: Any) {
    viewModelScope.launch {
      // Call surrounded by try-catch block to make handle exceptions caused by database
      try {
        userDBRepository.updateField(userId, field, value)
      } catch (e: Exception) {
        errorToast.showToast(ErrorType.ERROR_DATABASE)
        e.printStackTrace()
      }
    }
  }
}
