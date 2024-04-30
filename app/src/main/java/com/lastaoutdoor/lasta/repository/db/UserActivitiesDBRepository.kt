package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.user.UserActivity

/** This interface provides methods for interacting with user activities data. */
interface UserActivitiesDBRepository {

  suspend fun getUserActivities(userId: String): List<UserActivity>

  suspend fun addUserActivity(userId: String, userActivity: UserActivity)
}
