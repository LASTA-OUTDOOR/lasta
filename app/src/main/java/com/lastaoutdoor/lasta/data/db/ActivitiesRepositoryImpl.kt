package com.lastaoutdoor.lasta.data.db

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.data.model.Sports
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

    val userData = hashMapOf("Hiking" to arrayListOf<Trail>())
    userDocumentRef.set(userData, SetOptions.merge())
  }

  @Suppress("UNCHECKED_CAST")
  override suspend fun getUserActivities(user: FirebaseUser, activity: Sports): List<Trail> {
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

  override fun addTrailToUserActivities(user: FirebaseUser, trail: Trail) {
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
