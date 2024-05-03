package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository

class FakeActivitiesDBRepository() : ActivitiesDBRepository {
  override suspend fun addActivityIfNonExisting(activity: Activity): Boolean {
    return true
  }

  override suspend fun getActivityById(activityId: String): Activity? {
    return Activity(
        "id",
        1,
        ActivityType.BIKING,
        "activityImageUrl",
        Position(0.0, 0.0),
        2f,
        3,
    )
  }

  override suspend fun getActivitiesByIds(activityIds: List<String>): List<Activity> {
    return listOf(
        Activity(
            "id",
            1,
            ActivityType.BIKING,
            "activityImageUrl",
            Position(0.0, 0.0),
            2f,
            3,
        ))
  }

  override suspend fun getActivitiesByOSMIds(
      osmIds: List<Long>,
      onlyKnown: Boolean
  ): List<Activity> {
    return listOf(
        Activity(
            "id",
            1,
            ActivityType.BIKING,
            "activityImageUrl",
            Position(0.0, 0.0),
            2f,
            3,
        ))
  }

  override suspend fun updateStartPosition(activityId: String, position: Position) {
    // do nothing
  }
}
