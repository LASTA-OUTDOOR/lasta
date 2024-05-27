package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.api.Position
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository

class FakeActivitiesDBRepository() : ActivitiesDBRepository {

  var shouldThrowException = false

  override suspend fun addActivityIfNonExisting(activity: Activity): Boolean {
    if (shouldThrowException) {
      throw Exception("FakeActivitiesDBRepository: addActivityIfNonExisting failed")
    }
    return true
  }

  override suspend fun getActivityById(activityId: String): Activity? {
    if (shouldThrowException) {
      throw Exception("FakeActivitiesDBRepository: getActivityById failed")
    }
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
    if (shouldThrowException) {
      throw Exception("FakeActivitiesDBRepository: getActivitiesByIds failed")
    }
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
    if (shouldThrowException) {
      throw Exception("FakeActivitiesDBRepository: getActivitiesByOSMIds failed")
    }
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
    if (shouldThrowException) {
      throw Exception("FakeActivitiesDBRepository: updateStartPosition failed")
    }
  }

  override suspend fun addRating(activityId: String, rating: Rating, newMeanRating: String) {
    if (shouldThrowException) {
      throw Exception("FakeActivitiesDBRepository: addRating failed")
    }
  }

  override suspend fun deleteAllUserRatings(userId: String): List<Activity> {
    return emptyList()
  }
}
