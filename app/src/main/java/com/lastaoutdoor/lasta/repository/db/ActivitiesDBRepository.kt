package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.api.Position

/**
 * Repository for activities in the database.
 */
interface ActivitiesDBRepository {

  /**
   * Adds an activity to the database if it does not exist yet.
   *
   * @param activity The activity to add.
   * @return true if the activity was added, false if it already existed.
   */
  suspend fun addActivityIfNonExisting(activity: Activity): Boolean

  /**
   * Gets an activity by its ID.
   *
   * @param activityId The ID of the activity.
   * @return The activity with the given ID, or null if it does not exist.
   */
  suspend fun getActivityById(activityId: String): Activity?

  // suspend fun getActivityByOSMId(osmId: Long): Activity?

  /**
   * Gets activities by their IDs.
   *
   * @param activityIds The IDs of the activities.
   * @return The activities with the given IDs.
   */
  suspend fun getActivitiesByIds(activityIds: List<String>): List<Activity>

  /**
   * Gets activities by their OSM IDs.
   *
   * @param osmIds The OSM IDs of the activities.
   * @param onlyKnown If true, only known activities are returned.
   * @return The activities with the given OSM IDs.
   */
  suspend fun getActivitiesByOSMIds(osmIds: List<Long>, onlyKnown: Boolean): List<Activity>

  // suspend fun updateActivity(activity: Activity)

  /**
   * Updates the start position of an activity.
   *
   * @param activityId The ID of the activity.
   * @param position The new start position.
   */
  suspend fun updateStartPosition(activityId: String, position: Position)

  // suspend fun addRating(activity: Activity, rating: Rating)

  // suspend fun deleteRating(activity: Activity, userId: String)

  // suspend fun updateDifficulty(activity: Activity, difficulty: Difficulty)

  // suspend fun updateImageUrl(activity: Activity, imageUrl: String)
}
