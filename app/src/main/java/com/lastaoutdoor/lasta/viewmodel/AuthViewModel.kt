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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel class to handle authentication related operations
 *
 * @param googleAuth GoogleAuth instance to handle Google authentication
 * @return [HiltViewModel] instance
 */
@HiltViewModel
class AuthViewModel @Inject constructor(private val googleAuth: GoogleAuth) : ViewModel() {

  // State flow instance set to signed out by default
  private val _authStateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.SignedOut)
  val authStateFlow: StateFlow<AuthState> = _authStateFlow

  /**
   * Starts the Google sign-in process and if successful, updates the authStateFlow to
   * [AuthState.GoogleSignInIntent] with the intent sender
   */
  fun startGoogleSignIn() {
    viewModelScope.launch {
      try {
        val intentSender = googleAuth.signIn()
        _authStateFlow.update { AuthState.GoogleSignInIntent(intentSender) }
      } catch (e: Exception) {
        _authStateFlow.update { AuthState.Error(e.message ?: "An error occurred") }
      }
    }
  }

  /**
   * Handles the result of the Google sign-in process from an intent If the sign-in is successful,
   * updates the authStateFlow to [AuthState.Authenticated] with the signed-in user info. If the
   * sign-in fails, updates the authStateFlow to [AuthState.Error] with the error message
   *
   * @param intent the intent containing the result of the sign-in process
   */
  fun handleGoogleSignInResult(intent: Intent) {
    viewModelScope.launch {
      try {
        val signInResult = googleAuth.signInWithIntent(intent)
        if (signInResult.user != null) {
          _authStateFlow.update { AuthState.Authenticated(signInResult.user) }
        } else {
          _authStateFlow.update {
            AuthState.Error(signInResult.errorMessage ?: "Authentication failed")
          }
        }
      } catch (e: Exception) {
        _authStateFlow.update { AuthState.Error(e.message ?: "An error occurred") }
      }
    }
  }

  /**
   * Signs out the current user and updates the authStateFlow to [AuthState.SignedOut] If the
   * sign-out fails, updates the authStateFlow to [AuthState.Error] with the error message
   */
  fun signOut() {
    viewModelScope.launch {
      try {
        googleAuth.signOut()
        _authStateFlow.update { AuthState.SignedOut }
      } catch (e: Exception) {
        _authStateFlow.update { AuthState.Error(e.message ?: "Failed to sign out") }
      }
    }
  }

  /**
   * Tries to get current auth instance from Firebase and updates the authStateFlow to
   * [AuthState.Authenticated] with the user info if the user is signed in. Otherwise, updates the
   * authStateFlow to [AuthState.SignedOut]
   */
  fun fetchAuthInfo() {
    viewModelScope.launch {
      try {
        val user = googleAuth.getCurrentUser()
        if (user != null) {
          _authStateFlow.update { AuthState.Authenticated(user) }
        } else {
          _authStateFlow.update { AuthState.SignedOut }
        }
      } catch (e: Exception) {
        _authStateFlow.update { AuthState.Error(e.message ?: "An error occurred") }
      }
    }
  }

  /**
   * Helper method to check if the current state is [AuthState.GoogleSignInIntent] and if so,
   * returns the [IntentSender] from the authStateFlow
   *
   * @return the IntentSender instance if the state is [AuthState.GoogleSignInIntent], otherwise
   *   null
   */
  fun getIntentSender(): IntentSender? {
    return (authStateFlow.value as? AuthState.GoogleSignInIntent)?.intentSender
  }

  /**
   * Helper method to get the current user info if the state is [AuthState.Authenticated]
   *
   * @return the [UserModel] instance if the state is [AuthState.Authenticated], otherwise null
   */
  fun getCurrentUser(): UserModel? {
    return (authStateFlow.value as? AuthState.Authenticated)?.user
  }

  /** Enum class to represent the different authentication states */
  sealed class AuthState {
    /**
     * Represents the state where a google sign-in intent has been created
     *
     * @param intentSender the [IntentSender] instance to start the sign-in process
     */
    data class GoogleSignInIntent(val intentSender: IntentSender?) : AuthState()

    /**
     * Represents the state where a user has been authenticated and has valid user info
     *
     * @param user the [UserModel] instance representing the authenticated user
     */
    data class Authenticated(val user: UserModel) : AuthState()

    /** Represents the state where the user is/has signed out */
    object SignedOut : AuthState()

    /**
     * Represents the auth state where an error occurred
     *
     * @param errorMessage the error message
     */
    data class Error(val errorMessage: String) : AuthState()
  }
}
