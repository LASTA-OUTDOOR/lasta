package com.lastaoutdoor.lasta.repositories

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.lastaoutdoor.lasta.data.model.Trail

private const val ACTIVITIES_DB_NAME = "activities_database"

class ActivitiesRepository {
  private val database = Firebase.firestore

  fun addUserToActivitiesDatabase(user: FirebaseUser) {
    val userDocumentRef = database.collection(ACTIVITIES_DB_NAME).document(user.uid)

    val userData = hashMapOf("Hiking" to arrayListOf<Trail>())
    userDocumentRef.set(userData, SetOptions.merge())
  }
}
