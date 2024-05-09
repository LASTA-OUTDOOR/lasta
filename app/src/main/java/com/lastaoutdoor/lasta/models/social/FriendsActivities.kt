package com.lastaoutdoor.lasta.models.social

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel

data class FriendsActivities(
    val friend: UserModel,
    val userActivity: UserActivity,
    val activity: Activity
)
