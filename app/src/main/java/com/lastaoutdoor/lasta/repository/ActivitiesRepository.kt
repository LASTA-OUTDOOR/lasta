package com.lastaoutdoor.lasta.repository

import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType

interface ActivitiesRepository {
  fun addActivityToUserActivities(user: FirebaseUser, activity: ActivitiesDatabaseType)

  suspend fun getUserActivities(
      user: FirebaseUser,
      activityType: ActivitiesDatabaseType.Sports
  ): List<ActivitiesDatabaseType>
}
