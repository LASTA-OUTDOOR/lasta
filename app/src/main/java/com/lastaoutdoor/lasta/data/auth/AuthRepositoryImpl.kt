package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.db.DatabaseManager
import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.utils.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

@Singleton
class AuthRepositoryImpl
@Inject
constructor(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    @Named("signInRequest") private var signInRequest: BeginSignInRequest,
    @Named("signUpRequest") private var signUpRequest: BeginSignInRequest
) : AuthRepository {

  private val isSignUp: MutableStateFlow<Boolean> = MutableStateFlow(false)

  override fun observeIsSignUp(): Flow<Boolean> = isSignUp

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
      googleCredential: AuthCredential
  ): Flow<Response<UserModel>> = flow {
    try {
      emit(Response.Loading)
      val userCredential = auth.signInWithCredential(googleCredential).await()
      val user = userCredential.user
      if (user != null) {
        isSignUp.value = userCredential.additionalUserInfo?.isNewUser ?: false
        if (isSignUp.value) {
          // This is a sign-up, so create a new UserModel
          val userModel = UserModel(user, bio = "", HikingLevel.BEGINNER)

          // Add the user to the Firestore database
          DatabaseManager().addUserToDatabase(userModel)

          // Emit success response with UserModel
          emit(Response.Success(userModel))
        } else {
          // This is a sign-in, retrieve user data from Firestore
          val userModel = DatabaseManager().getUserFromDatabase(user.uid)
          if (userModel.userId.isNotEmpty()) {
            val newUserModel =
                UserModel(
                    userModel.userId,
                    user.displayName ?: "",
                    user.email ?: "",
                    user.photoUrl?.toString() ?: "",
                    userModel.bio,
                    userModel.hikingLevel)
            DatabaseManager().updateUserInfo(newUserModel)
            isSignUp.value = false
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
