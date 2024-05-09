package com.lastaoutdoor.lasta.data.offline

import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.offline.ActivityDao
import javax.inject.Inject

class ActivityDatabaseImpl @Inject constructor(private val dao: ActivityDao) {
  suspend fun insertActivity(a: Activity) {
    dao.insertActivity(a)
  }

  suspend fun getActivity(id: String): Activity {
    return dao.getActivity(id)
  }

  suspend fun deleteActivity(a: Activity) {
    dao.deleteActivity(a)
  }

  suspend fun getAllActivities(): List<Activity> {
    return dao.getAllActivities()
  }
}
