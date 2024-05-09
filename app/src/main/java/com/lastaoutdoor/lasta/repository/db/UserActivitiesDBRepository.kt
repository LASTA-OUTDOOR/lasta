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

  /**
   * Gets the n most recently finished user activities from the user with the given ID.
   *
   * @param userId the ID of the user
   * @param n the number of activities to get
   */
  suspend fun getNLatestActivities(userId: String, n: Int): List<UserActivity>

  /**
   * Returns all the hiking activities of the user with the given ID.
   *
   * @param userId the ID of the user
   * @return a list of hiking user activities
   */
  suspend fun getUserHikingActivities(userId: String): List<UserActivity>

  /**
   * Returns all the climbing activities of the user with the given ID.
   *
   * @param userId the ID of the user
   * @return a list of climbing user activities
   */
  suspend fun getUserClimbingActivities(userId: String): List<UserActivity>

  /**
   * Returns all the biking activities of the user with the given ID.
   *
   * @param userId the ID of the user
   * @return a list of biking user activities
   */
  suspend fun getUserBikingActivities(userId: String): List<UserActivity>
}
