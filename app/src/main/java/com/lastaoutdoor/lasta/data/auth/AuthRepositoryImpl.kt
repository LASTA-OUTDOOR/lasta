package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.db.DatabaseManager
import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.data.model.user.UserPreferences
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named("signInRequest")
    private var signInRequest: BeginSignInRequest,
    @Named("signUpRequest")
    private var signUpRequest: BeginSignInRequest
) : AuthRepository {


  override suspend fun startGoogleSignIn(): Flow<Response<BeginSignInResult>> = flow {
    try {
      val signInResult = oneTapClient.beginSignIn(signInRequest).await()
      emit(Response.Success(signInResult))
    } catch (e: Exception) {
      try {
        val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
        emit(Response.Success(signUpResult))
      } catch (e: Exception) {
        emit(Response.Failure(e))
      }
    }
  }

  override suspend fun finishGoogleSignIn(
    googleCredential: AuthCredential,
  ): Flow<Response<UserModel>> = flow {
    try {
      emit(Response.Loading)
      val userCredential = auth.signInWithCredential(googleCredential).await()
      val user = userCredential.user
      if (user != null) {
        val isSignUp = userCredential.additionalUserInfo?.isNewUser ?: false
        if (isSignUp) {
          // This is a sign-up, so create a new UserModel
          println("User is signing up")
          val userModel = UserModel(
            user.uid,
            user.displayName ?: "",
            user.email ?: "",
            user.photoUrl?.toString() ?: "",
            HikingLevel.BEGINNER
          )

          // Add the user to the Firestore database
          DatabaseManager().addUserToDatabase(userModel)

          // Emit success response with UserModel
          emit(Response.Success(userModel))
        } else {
          // This is a sign-in, retrieve user data from Firestore
          println("User is signing in")
          val userModel = DatabaseManager().getUserFromDatabase(user.uid)
          if (userModel.userId.isNotEmpty()) {
            val newUserModel = UserModel(
              userModel.userId,
              user.displayName ?: "",
              user.email ?: "",
              user.photoUrl?.toString() ?: "",
              userModel.hikingLevel
            )
            DatabaseManager().updateUserInfo(newUserModel)
            emit(Response.Success(newUserModel))
          } else {
            emit(Response.Failure(Exception("User data not found")))
          }
        }
      } else {
        emit(Response.Failure(Exception("User is null")))
      }
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
