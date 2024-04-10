package com.lastaoutdoor.lasta.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.utils.Response

typealias OneTapSignInResponse = Response<BeginSignInResult>

typealias SignInWithGoogleResponse = Response<UserModel>

typealias SignOutResponse = Response<Boolean>

interface AuthRepository {

  val currentUser: UserModel?

  suspend fun startGoogleSignIn(): OneTapSignInResponse

  suspend fun finishGoogleSignIn(googleCredential: AuthCredential): SignInWithGoogleResponse

  suspend fun signOut(): SignOutResponse
}
