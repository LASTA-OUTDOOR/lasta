package com.lastaoutdoor.lasta.data.offline

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.repository.offline.ActivityDao
import javax.inject.Inject

open class ActivityDatabaseImpl @Inject constructor(private val dao: ActivityDao) {
  // Download activity
  open suspend fun insertActivity(a: Activity) {
    dao.insertActivity(a)
  }
  // get downloaded activity
  open suspend fun getActivity(id: String): Activity {
    return dao.getActivity(id)
  }

  open suspend fun deleteActivity(a: Activity) {
    dao.deleteActivity(a)
  }

  open suspend fun getAllActivities(): List<Activity> {
    return dao.getAllActivities()
  }
  // Download completed climbing activity
  open suspend fun insertClimbingActivity(a: ClimbingUserActivity) {
    dao.insertClimbingActivity(a)
  }
  // Download completed hiking activity
  open suspend fun insertHikingActivity(a: HikingUserActivity) {
    dao.insertHikingActivity(a)
  }
  // Download completed biking activity
  open suspend fun insertBikingActivity(a: BikingUserActivity) {
    dao.insertBikingActivity(a)
  }
  // Download completed climbing activity
  open suspend fun insertUserPreferences(u: UserPreferences) {
    dao.insertUserPreferences(u)
  }

  open suspend fun getUserPreferences(): List<UserPreferences> {
    return dao.getUserPreferences()
  }
  // Get completed climbing activities

  open suspend fun getClimbingActivities(): List<ClimbingUserActivity> {
    return dao.getClimbingActivities()
  }
  // Get completed hiking activities

  open suspend fun getHikingActivities(): List<HikingUserActivity> {
    return dao.getHikingActivities()
  }
  // Get completed biking activities

  open suspend fun getBikingActivities(): List<BikingUserActivity> {
    return dao.getBikingActivities()
  }
}
