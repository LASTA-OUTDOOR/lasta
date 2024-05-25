package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.api.Position

/** Repository for activities in the database. */
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

  /**
   * Updates an activity's difficulty. The difficulty is augmented by one in a cycle.
   * Difficulty.EASY -> Difficulty.NORMAL -> Difficulty.HARD -> Difficulty.EASY
   *
   * @param activityId The ID of the activity.
   */
  suspend fun updateDifficulty(activityId: String)

  /**
   * Adds a rating to an activity.
   *
   * @param activityId The ID of the activity.
   * @param rating The rating to add.
   * @param newMeanRating The new mean rating of the activity.
   */
  fun addRating(activityId: String, rating: Rating, newMeanRating: String)

  /**
   * Deletes a rating from an activity.
   *
   * @param activity The activity to delete the rating from.
   * @param userId The ID of the user who rated the activity.
   * @return The activity with the rating deleted.
   */
  suspend fun deleteRating(activity: Activity, userId: String): Activity

  /**
   * Deletes all ratings of a user.
   *
   * @param userId The ID of the user.
   * @return The activities with the ratings deleted.
   */
  suspend fun deleteAllUserRatings(userId: String): List<Activity>

  // suspend fun updateDifficulty(activity: Activity, difficulty: Difficulty)

  // suspend fun updateImageUrl(activity: Activity, imageUrl: String)
}
