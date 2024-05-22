package com.lastaoutdoor.lasta.viewmodel.repo

import com.lastaoutdoor.lasta.data.offline.ActivityDatabaseImpl
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.offline.ActivityDao

class FakeActivityDatabaseImpl(val dao: ActivityDao) : ActivityDatabaseImpl(dao) {

  var shouldThrowException = false

  override suspend fun insertActivity(a: Activity) {
    if (shouldThrowException) {
      throw Exception("FakeActivityDatabaseImpl: insertActivity failed")
    }
  }

  override suspend fun getActivity(id: String): Activity {
    if (shouldThrowException) {
      throw Exception("FakeActivityDatabaseImpl: getActivity failed")
    }
    return Activity("id", 0)
  }

  override suspend fun deleteActivity(a: Activity) {
    if (shouldThrowException) {
      throw Exception("FakeActivityDatabaseImpl: deleteActivity failed")
    }
  }

  override suspend fun getAllActivities(): List<Activity> {
    if (shouldThrowException) {
      throw Exception("FakeActivityDatabaseImpl: getAllActivities failed")
    }
    return listOf(Activity("id", 0))
  }
}
