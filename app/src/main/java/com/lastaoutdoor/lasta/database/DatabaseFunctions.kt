package com.lastaoutdoor.lasta.database

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class DatabaseFunctions {

  // Attributes
  private val database = Firebase.firestore

  fun addUserToDatabase(user: FirebaseUser) {

    val userDocumentRef = database.collection("users").document(user.uid)

    // Create a data map with the user's information
    val userData =
        hashMapOf(
            "email" to user.email, "displayName" to user.displayName
            // Add any other user information you want to store here
            )

    userDocumentRef.set(userData, SetOptions.merge())
  }

  suspend fun getFieldFromUser(uid: String, field: String): String {
    // Create a reference to the document with the user's UID
    val userDocumentRef = database.collection("users").document(uid)

    // Get the document snapshot
    val documentSnapshot = userDocumentRef.get().await()

    // Get the field from the document snapshot
    return documentSnapshot.getString(field) ?: ""
  }

  fun addFieldToUser(uid: String, field: String, value: String) {
    // Create a reference to the document with the user's UID
    val userDocumentRef = database.collection("users").document(uid)

    // Create a data map with the field and value
    val data = hashMapOf(field to value)

    // Set the field in the document
    userDocumentRef.set(data, SetOptions.merge())
  }

  fun updateFieldInUser(uid: String, field: String, value: String) {
    // Create a reference to the document with the user's UID
    val userDocumentRef = database.collection("users").document(uid)

    // Create a data map with the field and value
    val data = hashMapOf(field to value)

    // Update the field in the document
    userDocumentRef.update(data as Map<String, Any>)
  }

  fun createCollection(collectionName: String) {
    database.collection(collectionName)
  }
}
