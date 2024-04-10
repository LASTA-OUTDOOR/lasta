package com.lastaoutdoor.lasta.data.db

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.repository.ActivitiesRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val ACTIVITIES_DB_NAME = "activities_database"

class ActivitiesRepositoryImpl @Inject constructor(private val database: FirebaseFirestore) :
    ActivitiesRepository {
  private val activityConverter = ActivityConverter()

  private fun addUserToActivitiesDatabase(user: FirebaseUser) {
    val userDocumentRef = database.collection(ACTIVITIES_DB_NAME).document(user.uid)
    val userData =
        hashMapOf(
            "Hiking" to arrayListOf<ActivitiesDatabaseType.Trail>(),
            "Climbing" to arrayListOf<ActivitiesDatabaseType.Climb>())
    userDocumentRef.set(userData, SetOptions.merge())
  }

  @Suppress("UNCHECKED_CAST")
  override suspend fun getUserActivities(
      user: FirebaseUser,
      activityType: ActivitiesDatabaseType.Sports
  ): List<ActivitiesDatabaseType> {
    val userDocumentRef = database.collection(ACTIVITIES_DB_NAME).document(user.uid)
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
        println("No 'Hiking' array found or it's not an array")
      }
    } else {
      println("No such document")
    }

    return trailList.toList()
  }

  override fun addActivityToUserActivities(user: FirebaseUser, activity: ActivitiesDatabaseType) {
    val userDocumentRef = database.collection(ACTIVITIES_DB_NAME).document(user.uid)

    CoroutineScope(Dispatchers.IO).launch {
      val documentSnapshot = userDocumentRef.get().await()

      val activityType: String = activity.sport.toString()
      if (!documentSnapshot.exists() || !documentSnapshot.contains(activityType))
          addUserToActivitiesDatabase(user)

      userDocumentRef.update(
          activityType, FieldValue.arrayUnion(activityConverter.activityToDatabase(activity)))
    }
  }
}
