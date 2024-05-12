package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
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

  private val activityConverter = ActivityConverter()

  @Suppress("UNCHECKED_CAST")
  override suspend fun getUserActivities(userId: String): List<UserActivity> {
    val userActivities: ArrayList<UserActivity> = ArrayList()

    val documentSnapshot = userActivitiesCollection.document(userId).get().await()
    if (documentSnapshot != null) {
      for (type in ActivityType.values()) {
        val activities = documentSnapshot.get(type.toString()) as? List<*>
        if (activities != null) {
          userActivities.addAll(
              activityConverter.databaseToActivity(activities as List<HashMap<String, Any>>, type))
        }
      }
    }

    return userActivities
  }

  override suspend fun addUserActivity(userId: String, userActivity: UserActivity) {
    val userActivitiesDocument = userActivitiesCollection.document(userId)
    val field = userActivity.activityType.toString()
    val updateMap = mapOf(field to FieldValue.arrayUnion(userActivity))
    userActivitiesDocument.set(updateMap, SetOptions.merge()).await()
  }

  // Be careful with take() behaviour when n is greater than the list size
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
