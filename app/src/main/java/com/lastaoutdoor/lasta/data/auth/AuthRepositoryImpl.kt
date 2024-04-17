package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.data.db.DatabaseManager
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.data.model.user.UserPreferences
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private var oneTapClient: SignInClient,
    private var signInRequest: BeginSignInRequest,
    private var signUpRequest: BeginSignInRequest
) : AuthRepository {


  override suspend fun startGoogleSignIn(isSignUp: Boolean): Flow<Response<BeginSignInResult>> = flow {
    try {
      emit(Response.Loading)
      val signInResult = if (isSignUp) {
        oneTapClient.beginSignIn(signUpRequest).await()
      } else {
        oneTapClient.beginSignIn(signInRequest).await()
      }
      emit(Response.Success(signInResult))
    } catch (e: Exception) {
      emit(Response.Failure(e))
    }
  }

  override suspend fun finishGoogleSignIn(
    googleCredential: AuthCredential,
    isSignUp: Boolean
  ): Flow<Response<UserModel>> = flow {
    try {
      emit(Response.Loading)
      val userCredential = auth.signInWithCredential(googleCredential).await()
      val user = userCredential.user
      if (user != null) {
        if (isSignUp) {
          // This is a sign-up, so create a new UserModel
          val userModel = UserModel(
            user.uid,
            user.displayName ?: "",
            user.email ?: "",
            user.photoUrl?.toString() ?: "",
            null
          )

          // Add the user to the Firestore database
          DatabaseManager().addUserToDatabase(userModel)

          // Emit success response with UserModel
          emit(Response.Success(userModel))
        } else {
          // This is a sign-in, retrieve user data from Firestore
          val userModel = DatabaseManager().getUserFromDatabase(user.uid)
          if (userModel.userId.isNotEmpty()) {
            // Emit success response with UserModel
            emit(Response.Success(userModel))
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
