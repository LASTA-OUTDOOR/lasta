package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Implementation of the ActivitiesRepository interface. This class provides methods to interact
 * with the Firebase Firestore database.
 *
 * @property database The Firebase Firestore database instance.
 * @property context The application context.
 */
class ActivitiesRepositoryImpl
@Inject
constructor(private val database: FirebaseFirestore, private val context: Context) :
    ActivitiesRepository {
  private val activityConverter = ActivityConverter()

  /**
   * Adds a new user to the activities database.
   *
   * @param user The FirebaseUser to add to the database.
   */
  private fun addUserToActivitiesDatabase(user: FirebaseUser) {
    try {
      val userDocumentRef =
          database
              .collection(context.getString(R.string.activities_database_name))
              .document(user.uid)
      val userData =
          hashMapOf(
              "Hiking" to arrayListOf<ActivitiesDatabaseType.Trail>(),
              "Climbing" to arrayListOf<ActivitiesDatabaseType.Climb>())
      userDocumentRef.set(userData, SetOptions.merge())
    } catch (e: Exception) {
      /* TODO: cache the information so that we can add activities when we have internet again */
      e.printStackTrace()
    }
  }

  /**
   * Fetches the activities of a user from the database.
   *
   * @param user The UserModel whose activities are to be fetched.
   * @param activityType The type of activities to fetch.
   * @return A list of activities of the specified type.
   */
  @Suppress("UNCHECKED_CAST")
  override suspend fun getUserActivities(
      user: UserModel,
      activityType: ActivitiesDatabaseType.Sports
  ): List<ActivitiesDatabaseType> {
    try {
      val userDocumentRef =
          database
              .collection(context.getString(R.string.activities_database_name))
              .document(user.userId)
      val trailList: ArrayList<ActivitiesDatabaseType> = arrayListOf()
      val document = userDocumentRef.get().await()

      if (document != null) {
        val activityArray = document.get(activityType.toString()) as? List<*>
        if (activityArray != null) {
          for (item in activityArray) {
            trailList.add(
                activityConverter.databaseToActivity(item as HashMap<String, Any>, activityType))
          }
        } else {
          return listOf()
        }
      } else {
        return listOf()
      }

      return trailList.toList()
    } catch (e: Exception) {
      e.printStackTrace()
      /* TODO: cache the information so that we can add activities when we have internet again */
      return listOf()
    }
  }

  /**
   * Adds an activity to a user's activities in the database.
   *
   * @param user The FirebaseUser to whom the activity is to be added.
   * @param activity The activity to add to the user's activities.
   */
  override fun addActivityToUserActivities(user: FirebaseUser, activity: ActivitiesDatabaseType) {
    try {
      val userDocumentRef =
          database
              .collection(context.getString(R.string.activities_database_name))
              .document(user.uid)

      CoroutineScope(Dispatchers.IO).launch {
        val documentSnapshot = userDocumentRef.get().await()

        val activityType: String = activity.sport.toString()
        if (!documentSnapshot.exists() || !documentSnapshot.contains(activityType))
            addUserToActivitiesDatabase(user)

        userDocumentRef.update(
            activityType, FieldValue.arrayUnion(activityConverter.activityToDatabase(activity)))
      }
    } catch (e: Exception) {
      e.printStackTrace()
      /* TODO: handle offline mode explicitly */
    }
  }
}
