package com.lastaoutdoor.lasta.data.model.user

import com.google.firebase.auth.FirebaseUser

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
