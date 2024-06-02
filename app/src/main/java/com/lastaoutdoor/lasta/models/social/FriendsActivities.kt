package com.lastaoutdoor.lasta.models.social

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel

/**
 * Data class representing the activities of a user's friends.
 *
 * @property friend The [UserModel] representing the friend.
 * @property userActivity The [UserActivity] object representing the friend's activity details.
 * @property activity The [Activity] object representing the specific activity.
 */
data class FriendsActivities(
    val friend: UserModel,
    val userActivity: UserActivity,
    val activity: Activity
)
