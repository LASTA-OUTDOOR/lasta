package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.time.TimeProvider
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.FriendsActivities
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.ActivitiesDBRepository
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import com.lastaoutdoor.lasta.repository.db.UserActivitiesDBRepository
import com.lastaoutdoor.lasta.utils.TimeFrame
import com.lastaoutdoor.lasta.utils.filterTrailsByTimeFrame
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Suppress("UNCHECKED_CAST")
@Singleton
class SocialDBRepositoryImpl
@Inject
constructor(
    context: Context,
    database: FirebaseFirestore,
    private val userActivitiesDBRepository: UserActivitiesDBRepository,
    private val timeProvider: TimeProvider,
    private val activitiesDBRepository: ActivitiesDBRepository
) : SocialDBRepository {
  private val userCollection = database.collection(context.getString(R.string.user_db_name))
  private val conversationCollection =
      database.collection(context.getString(R.string.conversations_db_name))

  override suspend fun getFriends(friendIds: List<String>): List<UserModel> {
    val friends: ArrayList<UserModel> = ArrayList()
    // Because when the friends list is super empty, the first element is an empty string
    if (friendIds.isEmpty()) return friends
    if (friendIds.first().isEmpty()) return friends
    val friendsQuery = userCollection.whereIn(FieldPath.documentId(), friendIds).get().await()
    if (!friendsQuery.isEmpty) {
      for (friend in friendsQuery.documents) {
        val senderId = friend.id
        val senderUserName = friend.getString("userName")!!
        val senderEmail = friend.getString("email")!!
        val senderDescription = friend.getString("description")!!
        val senderProfilePictureUrl = friend.getString("profilePictureUrl")!!
        val senderLanguage = friend.getString("language")!!
        val senderLanguagetoLanguage = Language.valueOf(senderLanguage)
        val prefActivity = friend.getString("prefActivity")!!
        val senderPrefActivity = ActivityType.valueOf(prefActivity)
        val levels = friend.get("levels") as HashMap<*, *>
        val senderLevels =
            UserActivitiesLevel(
                climbingLevel = UserLevel.valueOf(levels["climbingLevel"] as String? ?: "BEGINNER"),
                hikingLevel = UserLevel.valueOf(levels["hikingLevel"] as String? ?: "BEGINNER"),
                bikingLevel = UserLevel.valueOf(levels["bikingLevel"] as String? ?: "BEGINNER"))
        val senderFriendRequests = friend.get("friendRequests") as ArrayList<*>
        val senderFriends = friend.get("friends") as ArrayList<*>
        val senderFavorites = friend.get("favorites") as ArrayList<*>

        val friendModel =
            UserModel(
                userId = senderId,
                userName = senderUserName,
                email = senderEmail,
                profilePictureUrl = senderProfilePictureUrl,
                description = senderDescription,
                language = senderLanguagetoLanguage,
                prefActivity = senderPrefActivity,
                levels = senderLevels,
                friends = senderFriends as ArrayList<String>,
                friendRequests = senderFriendRequests as ArrayList<String>,
                favorites = senderFavorites as ArrayList<String>)
        friends.add(friendModel)
      }
    }

    return friends
  }

  override suspend fun getFriendRequests(userId: String): List<UserModel> {
    val friendRequests: ArrayList<UserModel> = ArrayList()
    val userDocument = userCollection.document(userId).get().await()
    val friendRequestIds = userDocument.get("friendRequests") as? ArrayList<String>
    if (!friendRequestIds.isNullOrEmpty()) {
      val friendRequestsQuery =
          userCollection.whereIn(FieldPath.documentId(), friendRequestIds).get().await()
      if (!friendRequestsQuery.isEmpty) {
        for (friendRequest in friendRequestsQuery.documents) {
          val senderId = friendRequest.id
          val senderUserName = friendRequest.getString("userName")!!
          val senderEmail = friendRequest.getString("email")!!
          val senderDescription = friendRequest.getString("description")!!
          val senderProfilePictureUrl = friendRequest.getString("profilePictureUrl")!!
          val senderLanguage = friendRequest.getString("language")!!
          val senderLanguagetoLanguage = Language.valueOf(senderLanguage)
          val prefActivity = friendRequest.getString("prefActivity")!!
          val senderPrefActivity = ActivityType.valueOf(prefActivity)
          val levels = friendRequest.get("levels") as HashMap<*, *>
          val senderLevels =
              UserActivitiesLevel(
                  climbingLevel =
                      UserLevel.valueOf(levels["climbingLevel"] as String? ?: "BEGINNER"),
                  hikingLevel = UserLevel.valueOf(levels["hikingLevel"] as String? ?: "BEGINNER"),
                  bikingLevel = UserLevel.valueOf(levels["bikingLevel"] as String? ?: "BEGINNER"))
          val senderFriendRequests = friendRequest.get("friendRequests") as ArrayList<*>
          val senderFriends = friendRequest.get("friends") as ArrayList<*>
          val senderFavorites = friendRequest.get("favorites") as ArrayList<*>

          val friendRequestModel =
              UserModel(
                  userId = senderId,
                  userName = senderUserName,
                  email = senderEmail,
                  profilePictureUrl = senderProfilePictureUrl,
                  description = senderDescription,
                  language = senderLanguagetoLanguage,
                  prefActivity = senderPrefActivity,
                  levels = senderLevels,
                  friends = senderFriends as ArrayList<String>,
                  friendRequests = senderFriendRequests as ArrayList<String>,
                  favorites = senderFavorites as ArrayList<String>)
          friendRequests.add(friendRequestModel)
        }
      }
    }

    return friendRequests
  }

  override suspend fun getLatestFriendActivities(
      userId: String,
      timeFrame: TimeFrame,
      friends: List<UserModel>
  ): List<FriendsActivities> {
    val activities: ArrayList<FriendsActivities> = ArrayList()
    for (friend in friends) {
      val friendActivities = userActivitiesDBRepository.getUserActivities(friend.userId)
      val filteredActivities = filterTrailsByTimeFrame(friendActivities, timeFrame, timeProvider)
      for (activity in filteredActivities) {
        activities.add(
            FriendsActivities(
                friend,
                activity,
                activitiesDBRepository.getActivityById(activity.activityId) as Activity))
      }
    }

    return activities.sortedByDescending { it.userActivity.timeStarted }
  }

  override suspend fun getConversation(
      user: UserModel,
      friend: UserModel,
      createNew: Boolean
  ): ConversationModel {

    // id is concatenation of userId and friendId, first is the smallest
    val id =
        if (user.userId < friend.userId) user.userId + friend.userId
        else friend.userId + user.userId

    // Try to find a document in the Firestore database with the conversation ID
    val conversationDocumentRef = conversationCollection.document(id)

    // If we didn't get any result, we create a new conversation
    val document = conversationDocumentRef.get().await()
    if (!document.exists() && createNew) {
      // store a conversation in the Firestore database
      val conversation =
          hashMapOf(
              "members" to listOf(user.userId, friend.userId),
              "messages" to emptyList<MessageModel>())

      // Store the conversation in the Firestore database
      conversationDocumentRef.set(conversation, SetOptions.merge())

      return ConversationModel(
          members = listOf(user, friend), messages = emptyList(), lastMessage = null)
    } else if (!document.exists()) {
      return ConversationModel(members = emptyList(), messages = emptyList(), lastMessage = null)
    }

    // chronological order of messages

    val messages: ArrayList<MessageModel> =
        (document.get("messages") as? ArrayList<*>)
            ?.map {
              val message = it as HashMap<*, *>
              MessageModel(
                  if (message["from"] == user.userId) user else friend,
                  message["content"] as String,
                  message["timestamp"] as Timestamp)
            }
            ?.sortedByDescending { it.timestamp }
            ?.toCollection(ArrayList()) ?: ArrayList()

    return ConversationModel(
        members = listOf(user, friend),
        messages = messages,
        lastMessage = if (messages.isEmpty()) null else messages.first())
  }

  override suspend fun getAllConversations(userId: String): List<ConversationModel> {
    val conversationsQuery =
        conversationCollection.whereArrayContains("members", userId).get().await()
    return conversationsQuery.documents.mapNotNull {
      val members = it.get("members") as? List<String>
      if (members != null) {
        val friendId = members.first { it != userId }
        val friend = userCollection.document(friendId).get().await()
        val friendModel =
            UserModel(
                userId = friend.id,
                userName = friend.getString("userName")!!,
                email = friend.getString("email")!!,
                profilePictureUrl = friend.getString("profilePictureUrl")!!,
                description = friend.getString("description")!!,
                language = Language.valueOf(friend.getString("language")!!),
                prefActivity = ActivityType.valueOf(friend.getString("prefActivity")!!),
                levels =
                    UserActivitiesLevel(
                        climbingLevel =
                            UserLevel.valueOf(friend.getString("climbingLevel") ?: "BEGINNER"),
                        hikingLevel =
                            UserLevel.valueOf(friend.getString("hikingLevel") ?: "BEGINNER"),
                        bikingLevel =
                            UserLevel.valueOf(friend.getString("bikingLevel") ?: "BEGINNER")),
                friends = friend.get("friends") as ArrayList<String>,
                friendRequests = friend.get("friendRequests") as ArrayList<String>,
                favorites = friend.get("favorites") as ArrayList<String>)
        val user = userCollection.document(userId).get().await()
        val userModel =
            UserModel(
                userId = user.id,
                userName = user.getString("userName")!!,
                email = user.getString("email")!!,
                profilePictureUrl = user.getString("profilePictureUrl")!!,
                description = user.getString("description")!!,
                language = Language.valueOf(user.getString("language")!!),
                prefActivity = ActivityType.valueOf(user.getString("prefActivity")!!),
                levels =
                    UserActivitiesLevel(
                        climbingLevel =
                            UserLevel.valueOf(user.getString("climbingLevel") ?: "BEGINNER"),
                        hikingLevel =
                            UserLevel.valueOf(user.getString("hikingLevel") ?: "BEGINNER"),
                        bikingLevel =
                            UserLevel.valueOf(user.getString("bikingLevel") ?: "BEGINNER")),
                friends = user.get("friends") as ArrayList<String>,
                friendRequests = user.get("friendRequests") as ArrayList<String>,
                favorites = user.get("favorites") as ArrayList<String>)
        getConversation(userModel, friendModel, false)
      } else null
    }
  }

  override suspend fun sendFriendRequest(userId: String, receiverId: String) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = userCollection.document(receiverId)

    // Store the uid of the sender in the user's document in an array in case there are multiple
    // requests (This does not allow duplicates)
    userDocumentRef.update("friendRequests", FieldValue.arrayUnion(userId)).await()
  }

  override suspend fun acceptFriendRequest(source: String, requester: String) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = userCollection.document(source)

    // Remove the requester from the friend requests array
    userDocumentRef.update("friendRequests", FieldValue.arrayRemove(requester)).await()

    // Add the requester to the friends array
    userDocumentRef.update("friends", FieldValue.arrayUnion(requester)).await()

    // Create a reference to the requester's document in the Firestore database
    val requesterDocumentRef = userCollection.document(requester)

    // Add the source to the requester's friends array
    requesterDocumentRef.update("friends", FieldValue.arrayUnion(source)).await()
  }

  override suspend fun declineFriendRequest(source: String, requester: String) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = userCollection.document(source)

    // Remove the requester from the friend requests array
    userDocumentRef.update("friendRequests", FieldValue.arrayRemove(requester)).await()
  }

  override suspend fun sendMessage(userId: String, friendUserId: String, message: String) {
    if (message.isEmpty() || userId.isEmpty() || friendUserId.isEmpty()) return

    // Create a reference to the conversation document in the Firestore database
    val id = if (userId < friendUserId) userId + friendUserId else friendUserId + userId

    val conversationDocumentRef = conversationCollection.document(id)

    // check if the conversation exists
    val document = conversationDocumentRef.get().await()
    if (!document.exists()) return

    // Add the message to the conversation
    conversationDocumentRef
        .update(
            "messages",
            FieldValue.arrayUnion(
                hashMapOf("from" to userId, "content" to message, "timestamp" to Timestamp.now())))
        .await()

    // Update the last message in the conversation
    conversationDocumentRef
        .update(
            "lastMessage",
            hashMapOf(
                "from" to userId,
                "content" to message,
                "timestamp" to FieldValue.serverTimestamp()))
        .await()
  }

    override suspend fun deleteAllConversations(userId: String) {
        val conversationsQuery = conversationCollection.whereArrayContains("members", userId).get().await()
        for (conversation in conversationsQuery.documents) {
            conversation.reference.delete().await()
        }
    }
}
