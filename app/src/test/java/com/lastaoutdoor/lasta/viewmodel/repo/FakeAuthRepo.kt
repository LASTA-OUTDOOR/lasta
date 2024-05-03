package com.lastaoutdoor.lasta.viewmodel.repo

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.auth.AuthRepository
import com.lastaoutdoor.lasta.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthRepo(override val isSignUp: Flow<Boolean>) : AuthRepository {
  override suspend fun startGoogleSignIn(): Flow<Response<BeginSignInResult>> {
    return flowOf(Response.Loading)
  }

  override suspend fun finishGoogleSignIn(
      googleCredential: AuthCredential
  ): Flow<Response<UserModel>> {
    return flowOf(Response.Success(UserModel("")))
  }

  override suspend fun signOut(): Flow<Response<Boolean>> {
    return flowOf((Response.Success(true)))
  }
}
