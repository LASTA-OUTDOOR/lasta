package com.lastaoutdoor.lasta.repository

import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.data.db.Trail
import com.lastaoutdoor.lasta.data.model.Sports

interface ActivitiesRepository {
  fun addTrailToUserActivities(user: FirebaseUser, trail: Trail)

  suspend fun getUserActivities(user: FirebaseUser, activity: Sports): List<Trail>
}
