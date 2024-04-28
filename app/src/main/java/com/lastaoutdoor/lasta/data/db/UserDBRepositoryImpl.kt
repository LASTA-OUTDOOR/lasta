package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDBRepositoryImpl @Inject constructor(private val context: Context, private val database: FirebaseFirestore) :
    UserDBRepository {

  private val userCollection = database.collection(context.getString(R.string.user_db_name))

  override suspend fun getUserById(userId: String): UserModel? {
    val user = userCollection.document(userId).get().await()
    return if (user.exists()) {
      val userName = user.getString("userName") ?: ""
      val email = user.getString("email") ?: ""
      val profilePictureUrl = user.getString("profilePictureUrl") ?: ""
      val description = user.getString("description") ?: ""
      val language = Language.valueOf(user.getString("language") ?: Language.ENGLISH.name)
      val prefActivity =
          ActivityType.valueOf(user.getString("prefActivity") ?: ActivityType.CLIMBING.name)
      val levels =
          UserActivitiesLevel(
              climbingLevel =
                  UserLevel.valueOf(user.getString("climbingLevel") ?: UserLevel.BEGINNER.name),
              hikingLevel =
                  UserLevel.valueOf(user.getString("hikingLevel") ?: UserLevel.BEGINNER.name),
              bikingLevel =
                  UserLevel.valueOf(user.getString("bikingLevel") ?: UserLevel.BEGINNER.name))
      UserModel(
          userId, userName, email, profilePictureUrl, description, language, prefActivity, levels)
    } else null
  }

  override suspend fun getUserByEmail(email: String): UserModel? {
    val query = userCollection.whereEqualTo("email", email).get().await()
    return if (!query.isEmpty) {
      val user = query.documents[0]
      val userId = user.id
      val userName = user.getString("userName") ?: ""
      val profilePictureUrl = user.getString("profilePictureUrl") ?: ""
      val description = user.getString("description") ?: ""
      val language = Language.valueOf(user.getString("language") ?: Language.ENGLISH.name)
      val prefActivity =
          ActivityType.valueOf(user.getString("prefActivity") ?: ActivityType.CLIMBING.name)
      val levels =
          UserActivitiesLevel(
              climbingLevel =
                  UserLevel.valueOf(user.getString("climbingLevel") ?: UserLevel.BEGINNER.name),
              hikingLevel =
                  UserLevel.valueOf(user.getString("hikingLevel") ?: UserLevel.BEGINNER.name),
              bikingLevel =
                  UserLevel.valueOf(user.getString("bikingLevel") ?: UserLevel.BEGINNER.name))
      UserModel(
          userId, userName, email, profilePictureUrl, description, language, prefActivity, levels)
    } else null
  }

  override fun updateUser(user: UserModel) {
    val userData =
        hashMapOf(
            "userName" to user.userName,
            "email" to user.email,
            "profilePictureUrl" to user.profilePictureUrl,
            "description" to user.description,
            "language" to user.language.name,
            "prefActivity" to user.prefActivity.name,
            "levels" to
                hashMapOf(
                    "climbingLevel" to user.levels.climbingLevel.name,
                    "hikingLevel" to user.levels.hikingLevel.name,
                    "bikingLevel" to user.levels.bikingLevel.name))
    userCollection
        .document(user.userId)
        .set(userData, SetOptions.merge())
        .addOnSuccessListener { /* TODO */}
        .addOnFailureListener { e -> e.printStackTrace() }
  }
  /*

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
            "hikingLevel" to preferences.userLevel)

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
  fun addFriendRequest(sender: String, user: UserModel) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = database.collection(USERS_COLLECTION).document(user.userId)

    // Store the uid of the sender in the user's document in an array in case there are multiple
    // requests (This does not allow duplicates)
    userDocumentRef.update("friendRequests", FieldValue.arrayUnion(sender))
  }

  /**
   * Function to get a user's friend requests from the Firestore database
   *
   * @param userId The unique identifier of the user
   * @return The user's friend requests
   */
  fun getFriendRequests(userId: String): List<UserModel> {

    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = database.collection(USERS_COLLECTION).document(userId)

    val friendsRequests: ArrayList<UserModel> = ArrayList()

    // Get the document snapshot
    runBlocking {
      val document = userDocumentRef.get().await()
      // Get the list (String) of friend requests from the document
      val friendRequests = document.get("friendRequests") as? List<*>
      // Get the corresponding user models from the list of friend requests
      if (friendRequests != null) {
        for (request in friendRequests) {
          val user = getUserFromDatabase(request as String)
          friendsRequests += user
        }
      }
    }

    return friendsRequests
  }

  /**
   * Function to accept a friend request in the Firestore database
   *
   * @param source The unique identifier of the user accepting the friend request
   * @param requester The unique identifier of the user who sent the friend request
   */
  fun acceptFriendRequest(source: String, requester: String) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = database.collection(USERS_COLLECTION).document(source)

    // Remove the requester from the friend requests array
    userDocumentRef.update("friendRequests", FieldValue.arrayRemove(requester))

    // Add the requester to the friends array
    userDocumentRef.update("friends", FieldValue.arrayUnion(requester))

    // Create a reference to the requester's document in the Firestore database
    val requesterDocumentRef = database.collection(USERS_COLLECTION).document(requester)

    // Add the source to the requester's friends array
    requesterDocumentRef.update("friends", FieldValue.arrayUnion(source))
  }

  /**
   * Function to decline a friend request in the Firestore database
   *
   * @param source The unique identifier of the user declining the friend request
   * @param requester The unique identifier of the user who sent the friend request
   */
  fun declineFriendRequest(source: String, requester: String) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = database.collection(USERS_COLLECTION).document(source)

    // Remove the requester from the friend requests array
    userDocumentRef.update("friendRequests", FieldValue.arrayRemove(requester))
  }

  /**
   * Function to get a user's friends from the Firestore database
   *
   * @param userId The unique identifier of the user
   * @return The user's friends
   */
  fun getFriends(userId: String): List<UserModel> {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = database.collection(USERS_COLLECTION).document(userId)

    // get the list of friends Id
    val friendsId = runBlocking { userDocumentRef.get().await().get("friends") as? List<*> }

    // get the corresponding user models from the list of friends
    val friends: ArrayList<UserModel> = ArrayList()
    if (friendsId != null) {
      for (friendId in friendsId) {
        val user = runBlocking { getUserFromDatabase(friendId as String) }
        friends += user
      }
    }

    return friends
  }

  /**
   * Function to get the conversation between 2 users
   *
   * @param userId The unique identifier of the user
   * @param friendId The unique identifier of the friend
   * @param createNew If true, create a new conversation if it doesn't exist
   * @return The conversation object between the user and the friend
   */
  fun getConversation(
      userId: String,
      friendId: String,
      createNew: Boolean = true
  ): ConversationModel {

    // id is concatenation of userId and friendId, first is the smallest
    val id = if (userId < friendId) userId + friendId else friendId + userId

    // Try to find a document in the Firestore database with the conversation ID
    val conversationDocumentRef = database.collection(CONVERSATION_COLLECTION).document(id)

    // If we didn't get any result, we create a new conversation
    var document = runBlocking { conversationDocumentRef.get().await() }
    if (!document.exists() && createNew) {

      println("null document")

      // store a conversation in the Firestore database
      val conversation =
          hashMapOf("members" to listOf(userId, friendId), "messages" to emptyList<MessageModel>())

      // Store the conversation in the Firestore database
      conversationDocumentRef.set(conversation, SetOptions.merge())

      document = runBlocking { conversationDocumentRef.get().await() }
    } else if (!document.exists()) {
      return ConversationModel(users = emptyList(), messages = emptyList(), lastMessage = null)
    }

    // Write the result in a ConversationModel object
    val user = runBlocking { getUserFromDatabase(userId) }
    val friend = runBlocking { getUserFromDatabase(friendId) }

    // chronological order of messages
    val messages: ArrayList<MessageModel>

    messages =
        (document.get("messages") as? ArrayList<*>)
            ?.map {
              val message = it as HashMap<*, *>
              MessageModel(
                  message["from"] as String,
                  message["content"] as String,
                  message["timestamp"] as Timestamp)
            }
            ?.sortedBy { it.timestamp }
            ?.toCollection(ArrayList()) ?: ArrayList()

    return if (messages.isEmpty()) {
      ConversationModel(users = listOf(user, friend), messages = messages, lastMessage = null)
    } else {
      ConversationModel(
          users = listOf(user, friend),
          messages = messages,
          lastMessage =
              MessageModel(
                  messages.last().from, messages.last().content, messages.last().timestamp))
    }
  }

  /**
   * Function to get all the created conversation on a user : name of the UserModel of the friend,
   * last message if there is one
   *
   * @param userId The unique identifier of the user
   * @param friends The list of friends of the user
   * @return The list of conversation preview
   */
  fun getAllConversation(userId: String, friends: List<UserModel>): List<ConversationModel> {
    val conversations: ArrayList<ConversationModel> = ArrayList()

    for (friend in friends) {
      val conversation = getConversation(userId, friend.userId, false)
      if (conversation.users.isEmpty()) continue
      conversations.add(conversation)
    }
    return conversations
  }

  /**
   * Function to send a message from a user to a friend
   *
   * @param userId The unique identifier of the user
   * @param friendUserId The unique identifier of the friend
   * @param message The message to send
   */
  fun sendMessage(userId: String, friendUserId: String, message: String) {
    // parameters check
    if (message.isEmpty() || userId.isEmpty() || friendUserId.isEmpty()) return

    println("Sending")

    // Create a reference to the conversation document in the Firestore database
    val id = if (userId < friendUserId) userId + friendUserId else friendUserId + userId

    val conversationDocumentRef = database.collection(CONVERSATION_COLLECTION).document(id)

    // check if the conversation exists
    val document = runBlocking { conversationDocumentRef.get().await() }
    if (!document.exists()) return

    println("Conversation exists")

    // Create a map containing the message
    val messageData: (Timestamp) -> HashMap<String, Any> = {
      hashMapOf("from" to userId, "content" to message, "timestamp" to it)
    }

    // Add the message to the conversation
    conversationDocumentRef.update("messages", FieldValue.arrayUnion(messageData(Timestamp.now())))

    // Update the last message in the conversation
    conversationDocumentRef.update("lastMessage", messageData(Timestamp.now()))

    println("Message sent")
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
              bio = prefSettings["bio"] as String,
              userLevel = prefSettings["hikingLevel"] as UserLevel,
              language = prefSettings["language"] as String,
              prefSport = prefSettings["prefSport"] as String)
        }
      }

      // Return default preferences if not found
      return UserPreferences(false, "", "", "", "", "", UserLevel.BEGINNER, "English", "Hiking")
    }
  }*/
}
