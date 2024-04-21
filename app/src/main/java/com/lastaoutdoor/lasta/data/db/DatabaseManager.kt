package com.lastaoutdoor.lasta.data.db

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.data.model.user.UserPreferences
import kotlinx.coroutines.tasks.await

private const val USERS_COLLECTION = "users"
private const val ACTIVITIES_COLLECTION = "activities_database"
private const val ERR_NOT_FOUND = -1.0

/** Class containing functions for interacting with the Firestore database */
class DatabaseManager(private val database: FirebaseFirestore = Firebase.firestore) {

  // Attributes
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
            "email" to user.email,
            "displayName" to user.userName,
            "profilePictureUrl" to user.profilePictureUrl,
            "hikingLevel" to user.hikingLevel.toString(),
            "friends" to emptyList<String>()
        )
    userDocumentRef.set(userData, SetOptions.merge())
  }

  suspend fun getUserFromDatabase(uid: String): UserModel {
    val userDocumentRef = database.collection(USERS_COLLECTION).document(uid)
    val document = userDocumentRef.get().await()
    if (document != null) {
      val email = document.getString("email") ?: ""
      val displayName = document.getString("displayName") ?: ""
      val profilePictureUrl = document.getString("profilePictureUrl") ?: ""
      var hikingLevel = document.getString("hikingLevel") ?: ""
      if (hikingLevel == "null" || hikingLevel == "") hikingLevel = "BEGINNER"
      return UserModel(uid, displayName, email, profilePictureUrl, HikingLevel.valueOf(hikingLevel))
    }
    return UserModel("", "", "", "", HikingLevel.BEGINNER)
  }

  suspend fun getUserFromEmail(mail: String) : UserModel? {
      val userDocumentRef = database.collection(USERS_COLLECTION)
        val query = userDocumentRef.whereEqualTo("email", mail).get().await()
        if (!query.isEmpty) {
            val document = query.documents[0]
            val email = document.getString("email") ?: ""
            val displayName = document.getString("displayName") ?: ""
            val profilePictureUrl = document.getString("profilePictureUrl") ?: ""
            var hikingLevel = document.getString("hikingLevel") ?: ""
            if (hikingLevel == "null" || hikingLevel == "") hikingLevel = "BEGINNER"
            return UserModel(document.id, displayName, email, profilePictureUrl, HikingLevel.valueOf(hikingLevel))
        }else{
            return null
        }
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

  fun updateUserInfo(user: UserModel) {
    val userDocumentRef = database.collection(USERS_COLLECTION).document(user.userId)

    // Create a data map with the user's information
    val userData =
        hashMapOf(
            "email" to user.email,
            "displayName" to user.userName,
            "profilePictureUrl" to user.profilePictureUrl,
            "hikingLevel" to user.hikingLevel.toString())
    userDocumentRef.set(userData, SetOptions.merge())
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
   * Function to get a field of a hiking activity from the user's document in the Firestore database
   *
   * @param user The user to get the field from
   * @param activityId The ID of the activity
   * @param field The field to get
   * @return The value of the field
   */
  @Suppress("UNCHECKED_CAST")
  suspend fun getFieldOfHiking(user: UserModel, activityId: Long, field: String): Any {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = database.collection(ACTIVITIES_COLLECTION).document(user.userId)

    // Get the document snapshot
    val document = userDocumentRef.get().await()

    if (document != null) {
      // Get the "Hiking" array from the document
      val hikingArray = document.get("Hiking") as? List<*>
      if (hikingArray != null) {
        for (item in hikingArray) {
          // Convert the item to a Trail object
          val trail =
              activityConverter.databaseToActivity(
                  item as HashMap<String, Any>, ActivitiesDatabaseType.Sports.HIKING)
                  as ActivitiesDatabaseType.Trail

          // Check if the activity ID matches
          if (trail.activityId == activityId) {
            // Convert the field name to HikingField enum
            val hikingField = HikingField.valueOf(field.uppercase())

            // Return the corresponding value based on the field
            return when (hikingField) {
              HikingField.AVG_SPEED_IN_KMH -> trail.avgSpeedInKMH
              HikingField.CALORIES_BURNED -> trail.caloriesBurned.toDouble()
              HikingField.DISTANCE_IN_METERS -> trail.distanceInMeters.toDouble()
              HikingField.ELEVATION_CHANGE_IN_METERS -> trail.elevationChangeInMeters.toDouble()
              HikingField.TIME_STARTED -> trail.timeStarted
              HikingField.TIME_FINISHED -> trail.timeFinished
            }
          }
        }
      }
    }
    return ERR_NOT_FOUND
  }

  /*
  /**
   * Function to set a hiking field from the user's document in the Firestore database
   *
   * @param uid The unique identifier of the user
   * @param field The field to set
   */
  @Suppress("UNCHECKED_CAST")
  fun setFieldOfHiking(user: UserModel, activityId: Long, field: String, value: Any) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = database.collection(ACTIVITIES_COLLECTION).document(user.userId)

    // Update the field in the document
    CoroutineScope(Dispatchers.IO).launch {
      val documentSnapshot = userDocumentRef.get().await()

      if (documentSnapshot != null) {
        val hikingArray = documentSnapshot.get("Hiking") as? List<*>
        if (hikingArray != null) {
          for (item in hikingArray) {
            val trail = activityConverter.databaseToActivity(item as HashMap<String, Any>, ActivitiesDatabaseType.Sports.HIKING) as ActivitiesDatabaseType.Trail
            if (trail.activityId == activityId) {
              // Convert the field name to HikingField enum
              val hikingField = HikingField.valueOf(field.uppercase())

              // Update the corresponding value based on the field
              when (hikingField) {
                HikingField.AVG_SPEED_IN_KMH -> trail.avgSpeedInKMH = value as Double
                HikingField.CALORIES_BURNED -> trail.caloriesBurned = value as Long
                HikingField.DISTANCE_IN_METERS -> trail.distanceInMeters = value as Long
                HikingField.ELEVATION_CHANGE_IN_METERS ->
                    trail.elevationChangeInMeters = value as Long
                HikingField.TIME_STARTED -> trail.timeStarted = value as Date
                HikingField.TIME_FINISHED -> trail.timeFinished = value as Date
              }
            }
          }
          // Update the "Hiking" array in the document
          userDocumentRef.update("Hiking", hikingArray)
        }
      }
    }
  }

   */

  /**
   * Function to update a user's preferences in the Firestore database
   *
   * @param user The user to update the preferences for
   * @param preferences The new preferences
   */
  fun updateUserPreferences(user: UserModel, preferences: UserPreferences) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = database.collection(USERS_COLLECTION).document(user.userId)

    // Create a map containing the user's preferences
    val prefSettings =
        hashMapOf(
            "isLoggedIn" to preferences.isLoggedIn,
            "uid" to preferences.uid,
            "userName" to preferences.userName,
            "email" to preferences.email,
            "profilePictureUrl" to preferences.profilePictureUrl,
            "hikingLevel" to preferences.hikingLevel)

    // Merge the preferences into the user's document
    val data = hashMapOf("prefSettings" to prefSettings)
    userDocumentRef.set(data, SetOptions.merge())
  }

    /**
     * Function that stores a friend request in the Firestore database
     *
     * @param sender The user Id of the sender (String)
     * @param user The user to add the friend request to
     * @return The user's preferences
     */
    fun addFriendRequest(sender : String, user: UserModel) {
        // Create a reference to the user's document in the Firestore database
        val userDocumentRef = database.collection(USERS_COLLECTION).document(user.userId)

        // Store the uid of the sender in the user's document in an array in case there are multiple requests (This does not allow duplicates)
        userDocumentRef.update("friendRequests", FieldValue.arrayUnion(sender))

    }

    /**
   * Function to get a user's preferences from the Firestore database
   *
   * @param uid The unique identifier of the user
   * @return The user's preferences
   */
  companion object {
    suspend fun getUserPreferences(uid: String): UserPreferences {
      // Create a reference to the user's document in the Firestore database
      val userDocumentRef = Firebase.firestore.collection(USERS_COLLECTION).document(uid)

      // Get the document snapshot
      val document = userDocumentRef.get().await()

      if (document != null) {
        val prefSettings = document.get("prefSettings") as? HashMap<*, *>
        if (prefSettings != null) {
          // Extract user preferences from the document
          return UserPreferences(
              isLoggedIn = prefSettings["isLoggedIn"] as Boolean,
              uid = prefSettings["uid"] as String,
              userName = prefSettings["userName"] as String,
              email = prefSettings["email"] as String,
              profilePictureUrl = prefSettings["profilePictureUrl"] as String,
              hikingLevel = prefSettings["hikingLevel"] as HikingLevel)
        }
      }
      // Return default preferences if not found
      return UserPreferences(false, "", "", "", "", HikingLevel.BEGINNER)
    }
  }
}
