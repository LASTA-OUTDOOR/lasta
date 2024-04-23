package com.lastaoutdoor.lasta.repository

import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel

interface ActivitiesRepository {
  fun addActivityToUserActivities(user: FirebaseUser, activity: ActivitiesDatabaseType)

  suspend fun getUserActivities(
      user: UserModel,
      activityType: ActivitiesDatabaseType.Sports
  ): List<ActivitiesDatabaseType>
}
