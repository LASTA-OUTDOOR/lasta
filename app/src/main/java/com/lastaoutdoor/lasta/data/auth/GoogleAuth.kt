package com.lastaoutdoor.lasta.data.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.user.UserModel
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.tasks.await

/**
 * Represents the result of a sign-in operation.
 *
 * @property user The signed-in user, if the operation was successful.
 * @property errorMessage The error message, if the operation was unsuccessful.
 */
data class SignInResult(val user: UserModel?, val errorMessage: String?)

/**
 * A class that handles Google sign-in operations.
 *
 * @property context The [Context] used to create the sign-in client.
 */
class GoogleAuth(private val context: Context) {
  /** The Firebase authentication instance. */
  private val auth = Firebase.auth

  private val signInClient: SignInClient = Identity.getSignInClient(context)

  /**
   * Builds a [BeginSignInRequest] object for Google sign-in.
   *
   * @return The request object.
   */
  private fun buildSignInRequest(): BeginSignInRequest {
    return BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build())
        .setAutoSelectEnabled(true)
        .build()
  }

  /**
   * Initiates the Google sign-in flow.
   *
   * @return The [IntentSender] for the sign-in flow, or 'null' if the operation failed.
   * @exception CancellationException Thrown if the operation was cancelled.
   */
  suspend fun signIn(): IntentSender? {
    val result =
        try {
          signInClient.beginSignIn(buildSignInRequest()).await()
        } catch (e: Exception) {
          e.printStackTrace()
          if (e is CancellationException) throw e
          null
        }
    return result?.pendingIntent?.intentSender
  }

  /**
   * Completes the Google sign-in flow.
   *
   * @param intent The [Intent] received from the sign-in flow.
   * @return The [SignInResult] of the sign-in operation.
   * @exception CancellationException Thrown if the operation was cancelled.
   */
  suspend fun signInWithIntent(intent: Intent): SignInResult {
    val credential = signInClient.getSignInCredentialFromIntent(intent)
    val googleIdToken = credential.googleIdToken
    val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
    return try {
      val user = auth.signInWithCredential(googleCredentials).await().user
      SignInResult(
          user =
              user?.run {
                UserModel(
                    userId = uid,
                    userName = displayName,
                    email = email,
                    profilePictureUrl = photoUrl?.toString())
              },
          errorMessage = null)
    } catch (e: Exception) {
      e.printStackTrace()
      if (e is CancellationException) throw e
      SignInResult(user = null, errorMessage = e.message)
    }
  }

  /**
   * Signs out the current user.
   *
   * @exception CancellationException Thrown if the operation was cancelled.
   */
  suspend fun signOut() {
    try {
      signInClient.signOut().await()
      auth.signOut()
    } catch (e: Exception) {
      e.printStackTrace()
      if (e is CancellationException) throw e
    }
  }

  /**
   * Gets the current signed-in user if there is one.
   *
   * @return The current user, or 'null' if no user is signed in.
   */
  fun getCurrentUser(): UserModel? =
      auth.currentUser?.run {
        UserModel(
            userId = uid,
            userName = displayName,
            email = email,
            profilePictureUrl = photoUrl?.toString())
      }
}
