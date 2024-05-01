package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.api.Position

interface ActivitiesDBRepository {
  suspend fun addActivityIfNonExisting(activity: Activity): Boolean

  suspend fun getActivityById(activityId: String): Activity

  suspend fun getActivityByOSMId(osmId: Long): Activity

  suspend fun getActivitiesByIds(activityIds: List<String>): List<Activity>

  suspend fun getActivitiesByOSMIds(osmIds: List<Long>, onlyKnown: Boolean): List<Activity>

  suspend fun updateActivity(activity: Activity)

  suspend fun updateStartPosition(activityId: String, position: Position)

  suspend fun addRating(activity: Activity, rating: Rating)

  suspend fun deleteRating(activity: Activity, userId: String)

  suspend fun updateDifficulty(activity: Activity, difficulty: Difficulty)

  suspend fun updateImageUrl(activity: Activity, imageUrl: String)
}
