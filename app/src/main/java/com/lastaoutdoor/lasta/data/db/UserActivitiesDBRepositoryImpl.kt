package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.user.BikingUserActivity
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.HikingUserActivity
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.repository.db.UserActivitiesDBRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class UserActivitiesDBRepositoryImpl
@Inject
constructor(context: Context, database: FirebaseFirestore) : UserActivitiesDBRepository {

  private val userActivitiesCollection =
      database.collection(context.getString(R.string.user_activities_db_name))

  override suspend fun getUserActivities(userId: String): List<UserActivity> {
    val userActivities: ArrayList<UserActivity> = ArrayList()

    val userActivitiesDocument = userActivitiesCollection.document(userId).get().await()
    if (userActivitiesDocument != null) {
      val climbingUserActivities = userActivitiesDocument.get("Climbing") as? List<*>
      userActivities.addAll(
          climbingUserActivities?.map { it as ClimbingUserActivity } ?: emptyList())
      val hikingUserActivities = userActivitiesDocument.get("Hiking") as? List<*>
      userActivities.addAll(hikingUserActivities?.map { it as HikingUserActivity } ?: emptyList())
      val bikingUserActivities = userActivitiesDocument.get("Biking") as? List<*>
      userActivities.addAll(bikingUserActivities?.map { it as BikingUserActivity } ?: emptyList())
    }

    return userActivities
  }

  override suspend fun addUserActivity(userId: String, userActivity: UserActivity) {
    val userActivitiesDocument = userActivitiesCollection.document(userId)
    val field = userActivity.activityType.toString()
    userActivitiesDocument.update(field, FieldValue.arrayUnion(userActivity)).await()
  }

  override suspend fun getNLatestActivities(userId: String, n: Int): List<UserActivity> {
    val userActivities = getUserActivities(userId)
    return userActivities.sortedByDescending { it.timeFinished }.take(n)
  }

  override suspend fun getUserHikingActivities(userId: String): List<UserActivity> {
    val userActivities = getUserActivities(userId)
    return userActivities.filterIsInstance<HikingUserActivity>()
  }

  override suspend fun getUserClimbingActivities(userId: String): List<UserActivity> {
    val userActivities = getUserActivities(userId)
    return userActivities.filterIsInstance<ClimbingUserActivity>()
  }

  override suspend fun getUserBikingActivities(userId: String): List<UserActivity> {
    val userActivities = getUserActivities(userId)
    return userActivities.filterIsInstance<BikingUserActivity>()
  }
}
