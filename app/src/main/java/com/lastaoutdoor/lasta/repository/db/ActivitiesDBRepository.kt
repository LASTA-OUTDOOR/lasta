package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating

interface ActivitiesDBRepository {
  suspend fun addActivity(activity: Activity): Activity

  suspend fun getActivityById(activityId: String): Activity

  suspend fun getActivityByOSMId(osmId: Long): Activity

  suspend fun getActivitiesByOSMIds(osmIds: List<Long>): List<Activity>

  suspend fun updateActivity(activity: Activity): Activity

  suspend fun addRating(activity: Activity, rating: Rating): Activity

  suspend fun deleteRating(activity: Activity, userId: String): Activity

  suspend fun updateDifficulty(activity: Activity, difficulty: Difficulty): Activity

  suspend fun updateImageUrl(activity: Activity, imageUrl: String): Activity
}
