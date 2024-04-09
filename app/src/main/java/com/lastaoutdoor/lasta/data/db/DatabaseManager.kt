package com.lastaoutdoor.lasta.data.db

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.lastaoutdoor.lasta.data.model.Sports
import com.lastaoutdoor.lasta.data.model.Trail
import com.lastaoutdoor.lasta.data.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val USERS_COLLECTION = "users"
private const val ACTIVITIES_COLLECTION = "activities_database"
private const val ERR_NOT_FOUND = -1.0;

/**
 * Class containing functions for interacting with the Firestore database
 */

class DatabaseManager {

  // Attributes
  private val database = Firebase.firestore
  private val activityConverter = ActivityConverter()

  /**
   * Function to add a user to the Firestore database
   *
   * @param user The user to add to the database
   *
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
     *
     * @return The value of the field
     *
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
     *
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
   *
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
     *
     */
  fun createCollection(collectionName: String) {
    database.collection(collectionName)
  }

    /**
     * Function to add a user to the activity database
     *
     * @param user The user to add to the activity database
     *
     */
  fun addUserToActivitiesDatabase(user: UserModel) {
    val userDocumentRef = database.collection(ACTIVITIES_COLLECTION).document(user.userId)
    val userData = hashMapOf("Hiking" to arrayListOf<Trail>())
    userDocumentRef.set(userData, SetOptions.merge())
  }

  /**
   * Function to add a trail to the user's activities in the Firestore database.
   *
   * @param user The user to add to the database
   * @param trail The trail to add to the user's activities
   *
   */
  fun addTrailToUserActivities(user: UserModel, trail: Trail) {
    val userDocumentRef = database.collection(ACTIVITIES_COLLECTION).document(user.userId)

    CoroutineScope(Dispatchers.IO).launch {
      val documentSnapshot = userDocumentRef.get().await()

      if (!documentSnapshot.exists() || !documentSnapshot.contains("Hiking"))
          addUserToActivitiesDatabase(user)

      userDocumentRef.update(
          "Hiking", FieldValue.arrayUnion(activityConverter.trailToDatabase(trail)))
    }
  }

    /**
     * Function to get a user's activities from the activity collection.
     *
     * @param user The user to get the activities for
     * @param activity The type of activity to get
     *
     * @return A list of trails for the specified activity
     *
     */
  @Suppress("UNCHECKED_CAST")
  suspend fun getUserActivities(user: UserModel, activity: Sports): List<Trail> {
    val userDocumentRef = database.collection(ACTIVITIES_COLLECTION).document(user.userId)
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

  /**
   * Function to get a field of a hiking activity from the user's document in the Firestore database
   *
   * @param user The user to get the field from
   * @param activityId The ID of the activity
   * @param field The field to get
   *
   * @return The value of the field
   *
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

}
