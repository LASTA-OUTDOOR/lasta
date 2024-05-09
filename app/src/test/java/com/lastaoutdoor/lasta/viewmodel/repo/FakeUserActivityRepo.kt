package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.repository.db.UserActivitiesDBRepository

class FakeUserActivityRepo() : UserActivitiesDBRepository {
  val fakeActivity = ClimbingUserActivity()

  override suspend fun getUserActivities(userId: String): List<UserActivity> {
    return listOf(fakeActivity)
  }

  override suspend fun addUserActivity(userId: String, userActivity: UserActivity) {}

  override suspend fun getNLatestActivities(userId: String, n: Int): List<UserActivity> {
    return emptyList()
  }

  override suspend fun getUserHikingActivities(userId: String): List<UserActivity> {
    return emptyList()
  }

  override suspend fun getUserClimbingActivities(userId: String): List<UserActivity> {
    return listOf(fakeActivity)
  }

  override suspend fun getUserBikingActivities(userId: String): List<UserActivity> {
    return emptyList()
  }
}
