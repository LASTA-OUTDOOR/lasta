package com.lastaoutdoor.lasta.data.db

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/**
 * Class containing functions for interacting with the Firebase Firestore database
 *
 * TODO: Rename this class to be more specific to the data it is handling
 * TODO: Make user model interact with the database otherwise no consistency
 */
class DatabaseFunctions {

  // Attributes
  private val database = Firebase.firestore

  /**
   * Function to add a user to the Firestore database
   *
   * @param user the FirebaseUser object representing the user
   *
   * TODO: This shouldn't be a [FirebaseUser] object, but a custom user model
   */
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
