package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.user.BikinUserActivity
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

  /**
   * Adds a new user to the activities database.
   *
   * @param userId the userId from the database.
   */

  /*
  private suspend fun addUserToActivitiesDatabase(userId: String) {
    try {
      val userDocumentRef = userActivitiesCollection.document(userId)
      val userData =
        hashMapOf(
          "Hiking" to arrayListOf<HikingUserActivity>(),
          "Climbing" to arrayListOf<ClimbingUserActivity>(),
          "Biking" to arrayListOf<BikingUserActivity>())
      userDocumentRef.set(userData).await()
    } catch (e: Exception) {
      /* TODO: cache the information so that we can add activities when we have internet again */
      e.printStackTrace()
    }
  }
   */

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
      userActivities.addAll(bikingUserActivities?.map { it as BikinUserActivity } ?: emptyList())
    }

    return userActivities
  }

  override suspend fun addUserActivity(userId: String, userActivity: UserActivity) {
    val userActivitiesDocument = userActivitiesCollection.document(userId)
    val field = userActivity.activityType.toString()
    userActivitiesDocument.update(field, FieldValue.arrayUnion(userActivity)).await()
  }
}
