package com.lastaoutdoor.lasta.viewmodel

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.data.auth.GoogleAuth
import com.lastaoutdoor.lasta.data.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(private val googleAuth: GoogleAuth) : ViewModel() {

  private val _authStateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.SignedOut)
  val authStateFlow: StateFlow<AuthState> = _authStateFlow

  fun startGoogleSignIn() {
    viewModelScope.launch {
      try {
        val intentSender = googleAuth.signIn()
        _authStateFlow.value = AuthState.GoogleSignInIntent(intentSender)
      } catch (e: Exception) {
        _authStateFlow.value = AuthState.Error(e.message ?: "An error occurred")
      }
    }
  }

  fun handleGoogleSignInResult(intent: Intent) {
    viewModelScope.launch {
      try {
        val signInResult = googleAuth.signInWithIntent(intent)
        if (signInResult.user != null) {
          _authStateFlow.value = AuthState.Authenticated(signInResult.user)
        } else {
          _authStateFlow.value =
              AuthState.Error(signInResult.errorMessage ?: "Authentication failed")
        }
      } catch (e: Exception) {
        _authStateFlow.value = AuthState.Error(e.message ?: "An error occurred")
      }
    }
  }

  fun signOut() {
    viewModelScope.launch {
      try {
        googleAuth.signOut()
        _authStateFlow.value = AuthState.SignedOut
      } catch (e: Exception) {
        _authStateFlow.value = AuthState.Error(e.message ?: "Failed to sign out")
      }
    }
  }

  fun fetchAuthInfo() {
    viewModelScope.launch {
      try {
        val user = googleAuth.getCurrentUser()
        if (user != null) {
          _authStateFlow.value = AuthState.Authenticated(user)
        } else {
          _authStateFlow.value = AuthState.SignedOut
        }
      } catch (e: Exception) {
        _authStateFlow.value = AuthState.Error(e.message ?: "An error occurred")
      }
    }
  }

  fun getIntentSender(): IntentSender? {
    return (authStateFlow.value as? AuthState.GoogleSignInIntent)?.intentSender
  }

  fun getCurrentUser(): UserModel? {
    return (authStateFlow.value as? AuthState.Authenticated)?.user
  }

  // Enum class to represent the authentication state
  sealed class AuthState {
    data class GoogleSignInIntent(val intentSender: IntentSender?) : AuthState()

    data class Authenticated(val user: UserModel) : AuthState()

    object SignedOut : AuthState()

    data class Error(val errorMessage: String) : AuthState()
  }
}
