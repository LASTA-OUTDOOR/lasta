package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var signInRequest: BeginSignInRequest,
) : AuthRepository {

  override suspend fun startGoogleSignIn(): Flow<Response<BeginSignInResult>> = flow {
    try {
      emit(Response.Loading)
      val signInResult = oneTapClient.beginSignIn(signInRequest).await()
      emit(Response.Success(signInResult))
    } catch (e: Exception) {
      emit(Response.Failure(e))
    }
  }

  override suspend fun finishGoogleSignIn(
      googleCredential: AuthCredential
  ): Flow<Response<UserModel>> = flow {
    try {
      emit(Response.Loading)
      val user = auth.signInWithCredential(googleCredential).await().user
      user?.run {
        emit(Response.Success(UserModel(uid, displayName, email, photoUrl.toString(), null)))
      } ?: run { emit(Response.Failure(Exception("User is null"))) }
    } catch (e: Exception) {
      emit(Response.Failure(e))
    }
  }

  override suspend fun signOut(): Flow<Response<Boolean>> = flow {
    try {
      emit(Response.Loading)
      oneTapClient.signOut().await()
      auth.signOut()
      emit(Response.Success(true))
    } catch (e: Exception) {
      emit(Response.Failure(e))
    }
  }
}
