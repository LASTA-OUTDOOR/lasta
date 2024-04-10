package com.lastaoutdoor.lasta.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.lastaoutdoor.lasta.data.db.ActivityConverter
import com.lastaoutdoor.lasta.data.model.profile.Sports
import com.lastaoutdoor.lasta.data.model.profile.Trail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val ACTIVITIES_DB_NAME = "activities_database"

class ActivitiesRepository {
  private val database = Firebase.firestore
  private val activityConverter = ActivityConverter()

  private fun addUserToActivitiesDatabase(user: FirebaseUser) {
    val userDocumentRef = database.collection(ACTIVITIES_DB_NAME).document(user.uid)

    val userData = hashMapOf("Hiking" to arrayListOf<Trail>())
    userDocumentRef.set(userData, SetOptions.merge())
  }

  @Suppress("UNCHECKED_CAST")
  suspend fun getUserActivities(user: FirebaseUser, activity: Sports): List<Trail> {
    val userDocumentRef = database.collection(ACTIVITIES_DB_NAME).document(user.uid)
    val trailList: ArrayList<Trail> = arrayListOf()
    val document = userDocumentRef.get().await()

    if (document != null) {
      val hikingArray = document.get(activity.toString()) as? List<*>
      if (hikingArray != null) {
        for (item in hikingArray) {
          trailList.add(activityConverter.databaseToTrail(item as HashMap<String, Any>))
        }
      } else {
        println("No 'Hiking' array found or it's not an array")
      }
    } else {
      println("No such document")
    }

    return trailList.toList()
  }

  fun addTrailToUserActivities(user: FirebaseUser, trail: Trail) {
    val userDocumentRef = database.collection(ACTIVITIES_DB_NAME).document(user.uid)

    CoroutineScope(Dispatchers.IO).launch {
      val documentSnapshot = userDocumentRef.get().await()

      if (!documentSnapshot.exists() || !documentSnapshot.contains("Hiking"))
          addUserToActivitiesDatabase(user)

      userDocumentRef.update(
          "Hiking", FieldValue.arrayUnion(activityConverter.trailToDatabase(trail)))
    }
  }
}
