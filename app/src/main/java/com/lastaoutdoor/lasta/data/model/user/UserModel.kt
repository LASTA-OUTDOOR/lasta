package com.lastaoutdoor.lasta.data.model.user

import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.data.model.activity.ActivityType

data class UserModel(
    val userId: String,
    val userName: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val description: String = "",
    val language: Language = Language.ENGLISH,
    val prefActivity: ActivityType = ActivityType.CLIMBING,
    val levels: UserActivitiesLevel =
        UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER)
) {
  constructor(
      user: FirebaseUser
  ) : this(
      userId = user.uid,
      userName = user.displayName ?: "",
      email = user.email ?: "",
      profilePictureUrl = user.photoUrl?.toString() ?: "")
}
