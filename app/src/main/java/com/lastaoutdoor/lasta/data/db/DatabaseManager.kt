package com.lastaoutdoor.lasta.data.db

import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.lastaoutdoor.lasta.data.model.UserModel
import com.lastaoutdoor.lasta.data.preferences.HikingLevel
import com.lastaoutdoor.lasta.data.preferences.UserPreferences
import java.util.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val USERS_COLLECTION = "users"
private const val ACTIVITIES_COLLECTION = "activities_database"
private const val ERR_NOT_FOUND = -1.0

/** Class containing functions for interacting with the Firestore database */
class DatabaseManager {

  // Attributes
  private val database = Firebase.firestore
  private val activityConverter = ActivityConverter()

  /**
   * Function to add a user to the Firestore database
   *
   * @param user The user to add to the database
   */
  fun addUserToDatabase(user: UserModel) {

    val userDocumentRef = database.collection(USERS_COLLECTION).document(user.userId)

    // Create a data map with the user's information
    val userData =
        hashMapOf(
            "uniqueId" to user.userId,
            "email" to user.email,
            "displayName" to user.userName,
            "profilePictureUrl" to user.profilePictureUrl
            // Add any other user information you want to store here
            )

    userDocumentRef.set(userData, SetOptions.merge())
  }

  /**
   * Function to get a field from the user's document in the Firestore database
   *
   * @param uid The unique identifier of the user
   * @param field The field to get
   * @return The value of the field
   */
  suspend fun getFieldFromUser(uid: String, field: String): String {
    // Create a reference to the document with the user's UID
    val userDocumentRef = database.collection(USERS_COLLECTION).document(uid)

    // Get the document snapshot
    val documentSnapshot = userDocumentRef.get().await()

    // Get the field from the document snapshot
    return documentSnapshot.getString(field) ?: ""
  }

  /**
   * Function to add a field to the user's document in the Firestore database
   *
   * @param uid The unique identifier of the user
   * @param field The field to add
   * @param value The value of the field
   */
  fun addFieldToUser(uid: String, field: String, value: String) {
    // Create a reference to the document with the user's UID
    val userDocumentRef = database.collection(USERS_COLLECTION).document(uid)

    // Create a data map with the field and value
    val data = hashMapOf(field to value)

    // Set the field in the document
    userDocumentRef.set(data, SetOptions.merge())
  }

  /**
   * Function to update a field in the user's document in the Firestore database
   *
   * @param uid The unique identifier of the user
   * @param field The field to update
   * @param value The new value of the field
   */
  fun updateFieldInUser(uid: String, field: String, value: String) {
    // Create a reference to the document with the user's UID
    val userDocumentRef = database.collection(USERS_COLLECTION).document(uid)

    // Create a data map with the field and value
    val data = hashMapOf(field to value)

    // Update the field in the document
    userDocumentRef.update(data as Map<String, Any>)
  }

  /**
   * Function to create a new collection in the Firestore database
   *
   * @param collectionName The name of the collection to create
   */
  fun createCollection(collectionName: String) {
    database.collection(collectionName)
  }

  /**
   * Function to update a user's preferences in the Firestore database
   *
   * @param user The user to update the preferences for
   * @param preferences The new preferences
   */
  fun updateUserPreferences(user: UserModel, preferences: UserPreferences) {
    val userDocumentRef = database.collection(USERS_COLLECTION).document(user.userId)
    val prefSettings =
        hashMapOf(
            "isLoggedIn" to preferences.isLoggedIn,
            "uid" to preferences.uid,
            "userName" to preferences.userName,
            "email" to preferences.email,
            "profilePictureUrl" to preferences.profilePictureUrl,
            "hikingLevel" to preferences.hikingLevel)
    val data = hashMapOf("prefSettings" to prefSettings)
    userDocumentRef.set(data, SetOptions.merge())
  }

  /**
   * Function to get a field of a hiking activity from the user's document in the Firestore database
   *
   * @param user The user to get the field from
   * @param activityId The ID of the activity
   * @param field The field to get
   * @return The value of the field
   */
  suspend fun getFieldOfHiking(user: UserModel, activityId: Long, field: String): Any {
    val userDocumentRef = database.collection(ACTIVITIES_COLLECTION).document(user.userId)
    val document = userDocumentRef.get().await()
    if (document != null) {
      val hikingArray = document.get("Hiking") as? List<*>
      if (hikingArray != null) {
        for (item in hikingArray) {
          val trail = activityConverter.databaseToTrail(item as HashMap<String, Any>)
          if (trail.activityID == activityId) {
            when (field) {
              "avgSpeedInKMH" -> return trail.avgSpeedInKMH
              "caloriesBurned" -> return trail.caloriesBurned.toDouble()
              "distanceInMeters" -> return trail.distanceInMeters.toDouble()
              "elevationChangeInMeters" -> return trail.elevationChangeInMeters.toDouble()
              "timeStarted" -> return trail.timeStarted
              "timeFinished" -> return trail.timeFinished
            }
          }
        }
      }
    }
    return ERR_NOT_FOUND
  }

  /**
   * Function to set a hiking field from the user's document in the Firestore database
   *
   * @param uid The unique identifier of the user
   * @param field The field to set
   */
  fun setFieldOfHiking(user: UserModel, activityId: Long, field: String, value: Any) {
    val userDocumentRef = database.collection(ACTIVITIES_COLLECTION).document(user.userId)
    CoroutineScope(Dispatchers.IO).launch {
      val documentSnapshot = userDocumentRef.get().await()
      if (documentSnapshot != null) {
        val hikingArray = documentSnapshot.get("Hiking") as? List<*>
        if (hikingArray != null) {
          for (item in hikingArray) {
            val trail = activityConverter.databaseToTrail(item as HashMap<String, Any>)
            if (trail.activityID == activityId) {
              when (field) {
                "avgSpeedInKMH" -> trail.avgSpeedInKMH = value as Double
                "caloriesBurned" -> trail.caloriesBurned = value as Long
                "distanceInMeters" -> trail.distanceInMeters = value as Long
                "elevationChangeInMeters" -> trail.elevationChangeInMeters = value as Long
                "timeStarted" -> trail.timeStarted = value as Date
                "timeFinished" -> trail.timeFinished = value as Date
              }
            }
          }
          userDocumentRef.update("Hiking", hikingArray)
        }
      }
    }
  }

  /**
   * Function to add a hiking activity to the user's document in the Firestore database
   *
   * @param user The user to add the activity for
   * @param trail The trail to add
   */
  companion object {
    suspend fun getUserPreferences(uid: String): UserPreferences {
      val userDocumentRef = Firebase.firestore.collection(USERS_COLLECTION).document(uid)
      val document = userDocumentRef.get().await()
      if (document != null) {
        val prefSettings = document.get("prefSettings") as? HashMap<String, Any>
        if (prefSettings != null) {
          return UserPreferences(
              isLoggedIn = prefSettings["isLoggedIn"] as Boolean,
              uid = prefSettings["uid"] as String,
              userName = prefSettings["userName"] as String,
              email = prefSettings["email"] as String,
              profilePictureUrl = prefSettings["profilePictureUrl"] as String,
              hikingLevel = prefSettings["hikingLevel"] as HikingLevel)
        }
      }
      return UserPreferences(false, "", "", "", "", HikingLevel.BEGINNER)
    }
  }
}
