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
import com.lastaoutdoor.lasta.data.model.UserModel
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.tasks.await

data class SignInResult(val user: UserModel?, val errorMessage: String?)

class GoogleAuth(private val context: Context) {
  private val auth = Firebase.auth

  private val signInClient: SignInClient = Identity.getSignInClient(context)

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

  suspend fun signOut() {
    try {
      signInClient.signOut().await()
      auth.signOut()
    } catch (e: Exception) {
      e.printStackTrace()
      if (e is CancellationException) throw e
    }
  }

  fun getCurrentUser(): UserModel? =
      auth.currentUser?.run {
        UserModel(
            userId = uid,
            userName = displayName,
            email = email,
            profilePictureUrl = photoUrl?.toString())
      }
}
