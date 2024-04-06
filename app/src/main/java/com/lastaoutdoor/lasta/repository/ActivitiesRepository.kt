package com.lastaoutdoor.lasta.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.lastaoutdoor.lasta.data.model.Sports
import com.lastaoutdoor.lasta.data.model.Trail
import kotlinx.coroutines.tasks.await

private const val ACTIVITIES_DB_NAME = "activities_database"

class ActivitiesRepository {
  private val database = Firebase.firestore

  fun addUserToActivitiesDatabase(user: FirebaseUser) {
    val userDocumentRef = database.collection(ACTIVITIES_DB_NAME).document(user.uid)

    val userData = hashMapOf("Hiking" to arrayListOf<Trail>())
    userDocumentRef.set(userData, SetOptions.merge())
  }

  @Suppress("UNCHECKED_CAST")
  suspend fun getUserActivities(user: FirebaseUser, activity: Sports): ArrayList<Trail> {
    // Create a reference to the document with the user's UID
    val userDocumentRef = database.collection("users").document(user.uid)

    // Get the document snapshot
    val documentSnapshot = userDocumentRef.get().await()

    // Get the field from the document snapshot
    return documentSnapshot.data?.get(activity.toString()) as ArrayList<Trail>
  }
}
