package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.user.UserActivity

/** This interface provides methods for interacting with user activities data. */
interface UserActivitiesDBRepository {

  /**
   * Returns all the activities of the user with the given ID.
   *
   * @param userId the ID of the user
   * @return a list of user activities
   */
  suspend fun getUserActivities(userId: String): List<UserActivity>

  /**
   * Adds a new user activity to the user with the given ID.
   *
   * @param userId the ID of the user
   * @param userActivity the user activity to add
   */
  suspend fun addUserActivity(userId: String, userActivity: UserActivity)
}
