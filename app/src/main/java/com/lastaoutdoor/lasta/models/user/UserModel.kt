package com.lastaoutdoor.lasta.models.user

import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.models.activity.ActivityType

data class UserModel(
    val userId: String,
    val userName: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val description: String = "",
    val language: Language = Language.ENGLISH,
    val prefActivity: ActivityType = ActivityType.CLIMBING,
    val levels: UserActivitiesLevel =
        UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER),
    val friends: List<String> = emptyList(),
    val friendRequests: List<String> = emptyList(),
    val favorites: List<String> = emptyList()
) {
  constructor(
      user: FirebaseUser
  ) : this(
      userId = user.uid,
      userName = user.displayName ?: "",
      email = user.email ?: "",
      profilePictureUrl = user.photoUrl?.toString() ?: "")

  fun copyUserWithFirebaseInfo(user: FirebaseUser): UserModel {
    return UserModel(
        user.uid,
        user.displayName ?: "",
        user.email ?: "",
        user.photoUrl?.toString() ?: "",
        description,
        language,
        prefActivity,
        levels,
        friends,
        friendRequests,
        favorites)
  }
}
