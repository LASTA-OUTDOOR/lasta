package com.lastaoutdoor.lasta.data.offline

import com.lastaoutdoor.lasta.models.activity.Activity
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
}
