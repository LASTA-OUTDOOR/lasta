package com.lastaoutdoor.lasta.data.model

import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.data.preferences.UserPreferences

/**
 * Data class representing a user
 *
 * @property userId the unique identifier of the user
 * @property userName the name of the user
 * @property email the email of the user
 * @property profilePictureUrl the URL of the user's profile picture
 * @property prefSettings the user's preferences
 *
 * TODO: Check the fields are in sync with what the app is supposed to store and display
 */
data class UserModel(
    val userId: String,
    val userName: String?,
    val email: String?,
    val profilePictureUrl: String?,
    val prefSettings: UserPreferences
) {
  constructor(
      firebaseUser: FirebaseUser,
      prefSettings: UserPreferences
  ) : this(
      firebaseUser.uid,
      firebaseUser.displayName,
      firebaseUser.email,
      firebaseUser.photoUrl?.toString(),
      prefSettings)
}
