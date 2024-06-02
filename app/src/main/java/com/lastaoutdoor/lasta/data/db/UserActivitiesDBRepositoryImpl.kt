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

/**
 * UserActivitiesDBRepositoryImpl is a class that provides methods to manage user activities in a Firebase Firestore database.
 * It implements the UserActivitiesDBRepository interface.
 *
 * @property userActivitiesCollection The Firestore collection reference where user activities are stored.
 * @property activityConverter An instance of ActivityConverter to handle activity data conversion.
 * @constructor Creates an instance of UserActivitiesDBRepositoryImpl with the specified context and Firestore database.
 *
 * @param context The application context, used to access resources.
 * @param database The FirebaseFirestore instance to interact with Firestore.
 */
@Singleton
class UserActivitiesDBRepositoryImpl
@Inject
constructor(context: Context, database: FirebaseFirestore) : UserActivitiesDBRepository {

  // Reference to the Firestore collection where user activities are stored
  private val userActivitiesCollection =
    database.collection(context.getString(R.string.user_activities_db_name))

  // Converter to handle activity data conversion
  private val activityConverter = ActivityConverter()

  /**
   * Retrieves all user activities from the Firestore database for the specified user ID.
   *
   * @param userId The ID of the user whose activities are to be retrieved.
   * @return A list of UserActivity objects.
   */
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

  /**
   * Adds a user activity to the Firestore database.
   *
   * @param userId The ID of the user.
   * @param userActivity The activity to be added.
   */
  override suspend fun addUserActivity(userId: String, userActivity: UserActivity) {
    val userActivitiesDocument = userActivitiesCollection.document(userId)
    val field = userActivity.activityType.toString()
    val updateMap = mapOf(field to FieldValue.arrayUnion(userActivity))
    userActivitiesDocument.set(updateMap, SetOptions.merge()).await()
  }

  /**
   * Retrieves the latest n user activities from the Firestore database for the specified user ID.
   * The activities are sorted by timeFinished in descending order.
   *
   * @param userId The ID of the user.
   * @param n The number of latest activities to retrieve.
   * @return A list of the latest n UserActivity objects.
   */
  override suspend fun getNLatestActivities(userId: String, n: Int): List<UserActivity> {
    val userActivities = getUserActivities(userId)
    return userActivities.sortedByDescending { it.timeFinished }.take(n)
  }

  /**
   * Retrieves all hiking activities of a user from the Firestore database for the specified user ID.
   *
   * @param userId The ID of the user.
   * @return A list of HikingUserActivity objects.
   */
  override suspend fun getUserHikingActivities(userId: String): List<UserActivity> {
    val userActivities = getUserActivities(userId)
    return userActivities.filterIsInstance<HikingUserActivity>()
  }

  /**
   * Retrieves all climbing activities of a user from the Firestore database for the specified user ID.
   *
   * @param userId The ID of the user.
   * @return A list of ClimbingUserActivity objects.
   */
  override suspend fun getUserClimbingActivities(userId: String): List<UserActivity> {
    val userActivities = getUserActivities(userId)
    return userActivities.filterIsInstance<ClimbingUserActivity>()
  }

  /**
   * Retrieves all biking activities of a user from the Firestore database for the specified user ID.
   *
   * @param userId The ID of the user.
   * @return A list of BikingUserActivity objects.
   */
  override suspend fun getUserBikingActivities(userId: String): List<UserActivity> {
    val userActivities = getUserActivities(userId)
    return userActivities.filterIsInstance<BikingUserActivity>()
  }

  /**
   * Deletes all user activities from the Firestore database for the specified user ID.
   *
   * @param userId The ID of the user whose activities are to be deleted.
   */
  override suspend fun deleteUserActivities(userId: String) {
    userActivitiesCollection.document(userId).delete().await()
  }
}
