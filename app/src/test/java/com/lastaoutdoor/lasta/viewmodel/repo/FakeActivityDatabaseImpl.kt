package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.data.offline.ActivityDatabaseImpl
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.offline.ActivityDao

class FakeActivityDatabaseImpl(val dao: ActivityDao) : ActivityDatabaseImpl(dao) {
  override suspend fun insertActivity(a: Activity) {}

  override suspend fun getActivity(id: String): Activity {
    return Activity("id", 0)
  }

  override suspend fun deleteActivity(a: Activity) {}

  override suspend fun getAllActivities(): List<Activity> {
    return listOf(Activity("id", 0))
  }
}
