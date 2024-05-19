package com.lastaoutdoor.lasta.viewmodel.repo

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.auth.AuthRepository
import com.lastaoutdoor.lasta.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthRepo(override val isSignUp: Flow<Boolean>) : AuthRepository {

  var shouldThrowException = false

  override suspend fun startGoogleSignIn(): Flow<Response<BeginSignInResult>> {
    if (shouldThrowException) {
      throw Exception("FakeAuthRepo: startGoogleSignIn failed")
    }
    return flowOf(Response.Loading)
  }

  override suspend fun finishGoogleSignIn(
      googleCredential: AuthCredential
  ): Flow<Response<UserModel>> {
    if (shouldThrowException) {
      throw Exception("FakeAuthRepo: finishGoogleSignIn failed")
    }
    return flowOf(Response.Success(UserModel("")))
  }

  override suspend fun signOut(): Flow<Response<Boolean>> {
    if (shouldThrowException) {
      throw Exception("FakeAuthRepo: signOut failed")
    }
    return flowOf((Response.Success(true)))
  }
}
