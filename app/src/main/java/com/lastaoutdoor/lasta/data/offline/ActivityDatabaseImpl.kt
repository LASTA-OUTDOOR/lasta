package com.lastaoutdoor.lasta.data.offline

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.repository.offline.ActivityDao
import javax.inject.Inject

open class ActivityDatabaseImpl @Inject constructor(private val dao: ActivityDao) {
  open suspend fun insertActivity(a: Activity) {
    dao.insertActivity(a)
  }

  open suspend fun getActivity(id: String): Activity {
    return dao.getActivity(id)
  }

  open suspend fun deleteActivity(a: Activity) {
    dao.deleteActivity(a)
  }

  open suspend fun getAllActivities(): List<Activity> {
    return dao.getAllActivities()
  }

  open suspend fun insertClimbingActivity(a: ClimbingUserActivity) {
    dao.insertClimbingActivity(a)
  }

  open suspend fun insertHikingActivity(a: HikingUserActivity) {
    dao.insertHikingActivity(a)
  }

  open suspend fun insertBikingActivity(a: BikingUserActivity) {
    dao.insertBikingActivity(a)
  }

  open suspend fun insertUserPreferences(u: UserPreferences) {
    dao.insertUserPreferences(u)
  }

  open suspend fun getUserPreferences(): List<UserPreferences> {
    return dao.getUserPreferences()
  }

  open suspend fun getClimbingActivities(): List<ClimbingUserActivity> {
    return dao.getClimbingActivities()
  }

  open suspend fun getHikingActivities(): List<HikingUserActivity> {
    return dao.getHikingActivities()
  }

  open suspend fun getBikingActivities(): List<BikingUserActivity> {
    return dao.getBikingActivities()
  }
}
