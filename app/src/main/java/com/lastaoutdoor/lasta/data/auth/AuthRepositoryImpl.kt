package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.repository.OneTapSignInResponse
import com.lastaoutdoor.lasta.repository.SignInWithGoogleResponse
import com.lastaoutdoor.lasta.repository.SignOutResponse
import com.lastaoutdoor.lasta.utils.Response.Failure
import com.lastaoutdoor.lasta.utils.Response.Success
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class AuthRepositoryImpl
@Inject
constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var signInRequest: BeginSignInRequest,
) : AuthRepository {
  override val isUserAuthentificated = auth.currentUser != null

  override val currentUser: UserModel?
    get() =
        auth.currentUser?.let {
          UserModel(
              userId = it.uid,
              userName = it.displayName,
              email = it.email,
              profilePictureUrl = it.photoUrl.toString())
        }

  override suspend fun startGoogleSignIn(): OneTapSignInResponse {
    return try {
      val signInResult = oneTapClient.beginSignIn(signInRequest).await()
      Success(signInResult)
    } catch (e: Exception) {
      Failure(e)
    }
  }

  override suspend fun finishGoogleSignIn(
      googleCredential: AuthCredential
  ): SignInWithGoogleResponse {
    return try {
      val user = auth.signInWithCredential(googleCredential).await().user
      user?.run { Success(UserModel(uid, displayName, email, photoUrl.toString())) }
          ?: run { Failure(Exception("User is null")) }
    } catch (e: Exception) {
      Failure(e)
    }
  }

  override suspend fun signOut(): SignOutResponse {
    return try {
      oneTapClient.signOut().await()
      auth.signOut()
      Success(true)
    } catch (e: Exception) {
      Failure(e)
    }
  }
}
