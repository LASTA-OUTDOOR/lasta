package com.lastaoutdoor.lasta.repository.auth

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.utils.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

  val isSignUp: Flow<Boolean>

  suspend fun startGoogleSignIn(): Flow<Response<BeginSignInResult>>

  suspend fun finishGoogleSignIn(googleCredential: AuthCredential): Flow<Response<UserModel>>

  suspend fun signOut(): Flow<Response<Boolean>>

  suspend fun deleteAccount(): Flow<Response<Boolean>>
}
