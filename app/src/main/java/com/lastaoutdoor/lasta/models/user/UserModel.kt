package com.lastaoutdoor.lasta.models.user

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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

  // Returns the description text for the preferred activity and level
  @Composable
  fun descrText(): String {
    val prefActivity = this.prefActivity
    val prefActivityLevel =
        when (prefActivity) {
          ActivityType.CLIMBING -> this.levels.climbingLevel
          ActivityType.HIKING -> this.levels.hikingLevel
          ActivityType.BIKING -> this.levels.bikingLevel
        }
    return prefActivity.resourcesToString(LocalContext.current) +
        " " +
        prefActivityLevel.resourcesToString(LocalContext.current)
  }

  // returns the number of friends in common with the given friendlist
  fun getCommonFriends(friendList: List<UserModel>): Int {
    return this.friends.intersect(friendList.map { it.userId }.toSet()).size
  }
}
