package com.lastaoutdoor.lasta.data.model.user

import com.google.firebase.auth.FirebaseUser

/**
 * Data class representing a user
 *
 * @property userId the unique identifier of the user
 * @property userName the name of the user
 * @property email the email of the user
 * @property profilePictureUrl the URL of the user's profile picture
 * @property bio the bio of the user
 *
 * TODO: Make all model files match with database so that updating and writing will be easier
 */
data class UserModel(
    val userId: String,
    val userName: String?,
    val email: String?,
    val profilePictureUrl: String?,
    val bio: String?,
    val userLevel: UserLevel
) {
  constructor(
      firebaseUser: FirebaseUser,
      bio: String,
      hikingLevel: UserLevel
  ) : this(
      firebaseUser.uid,
      firebaseUser.displayName,
      firebaseUser.email,
      firebaseUser.photoUrl?.toString(),
      bio,
      hikingLevel)
}
