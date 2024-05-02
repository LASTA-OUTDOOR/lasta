package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialDBRepositoryImpl @Inject constructor(context: Context, database: FirebaseFirestore) :
    SocialDBRepository {
  private val userCollection = database.collection(context.getString(R.string.user_db_name))
  private val conversationCollection =
      database.collection(context.getString(R.string.conversations_db_name))

  override suspend fun getFriends(friendIds: List<String>): List<UserModel> {
    val friends: ArrayList<UserModel> = ArrayList()
    // Because when the friends list is super empty, the first element is an empty string
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
        val levels = friend.get("levels") as HashMap<String, String> ?: HashMap()
        val senderLevels =
            UserActivitiesLevel(
                climbingLevel = UserLevel.valueOf(levels["climbingLevel"] ?: "BEGINNER"),
                hikingLevel = UserLevel.valueOf(levels["hikingLevel"] ?: "BEGINNER"),
                bikingLevel = UserLevel.valueOf(levels["bikingLevel"] ?: "BEGINNER"))
        val senderFriendRequests = friend.get("friendRequests") as ArrayList<String>
        val senderFriends = friend.get("friends") as ArrayList<String>
        val senderFavorites = friend.get("favorites") as ArrayList<String>

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
                friends = senderFriends,
                friendRequests = senderFriendRequests,
                favorites = senderFavorites)
        friends.add(friendModel)
      }
    }

    return friends
  }

  override suspend fun getFriendRequests(userId: String): List<UserModel> {
    val friendRequests: ArrayList<UserModel> = ArrayList()
    val userDocument = userCollection.document(userId).get().await()
    val friendRequestIds = userDocument.get("friendRequests") as? ArrayList<String>
    if (friendRequestIds != null && friendRequestIds.isNotEmpty()) {
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
          val levels = friendRequest.get("levels") as HashMap<String, String>
          val senderLevels =
              UserActivitiesLevel(
                  climbingLevel = UserLevel.valueOf(levels["climbingLevel"] ?: "BEGINNER"),
                  hikingLevel = UserLevel.valueOf(levels["hikingLevel"] ?: "BEGINNER"),
                  bikingLevel = UserLevel.valueOf(levels["bikingLevel"] ?: "BEGINNER"))
          val senderFriendRequests = friendRequest.get("friendRequests") as ArrayList<String>
          val senderFriends = friendRequest.get("friends") as ArrayList<String>
          val senderFavorites = friendRequest.get("favorites") as ArrayList<String>

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
                  friends = senderFriends,
                  friendRequests = senderFriendRequests,
                  favorites = senderFavorites)
          friendRequests.add(friendRequestModel)
        }
      }
    }

    return friendRequests
  }

  override suspend fun getLatestFriendActivities(userId: String, days: Int): List<UserActivity> {
    return emptyList()
  }

  override suspend fun getConversation(
      userId: String,
      friendId: String,
      createNew: Boolean
  ): ConversationModel {

    // id is concatenation of userId and friendId, first is the smallest
    val id = if (userId < friendId) userId + friendId else friendId + userId

    // Try to find a document in the Firestore database with the conversation ID
    val conversationDocumentRef = conversationCollection.document(id)

    // If we didn't get any result, we create a new conversation
    val document = conversationDocumentRef.get().await()
    if (!document.exists() && createNew) {
      // store a conversation in the Firestore database
      val conversation =
          hashMapOf("members" to listOf(userId, friendId), "messages" to emptyList<MessageModel>())

      // Store the conversation in the Firestore database
      conversationDocumentRef.set(conversation, SetOptions.merge())

      return ConversationModel(
          members = listOf(userId, friendId), messages = emptyList(), lastMessage = null)
    } else if (!document.exists()) {
      return ConversationModel(members = emptyList(), messages = emptyList(), lastMessage = null)
    }

    // chronological order of messages

    val messages: ArrayList<MessageModel> =
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

    return ConversationModel(
        members = listOf(userId, friendId),
        messages = messages,
        lastMessage = if (messages.isEmpty()) null else messages.last())
  }

  override suspend fun getAllConversations(userId: String): List<ConversationModel> {

    val conversationsQuery =
        conversationCollection.whereArrayContains("members", userId).get().await()
    return conversationsQuery.documents.mapNotNull { it.toObject(ConversationModel::class.java) }
  }

  override suspend fun sendFriendRequest(userId: String, receiverId: String): Unit {
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

    // Create a map containing the message
    val messageData: (Timestamp) -> HashMap<String, Any> = {
      hashMapOf("from" to userId, "content" to message, "timestamp" to it)
    }

    // Add the message to the conversation
    conversationDocumentRef
        .update("messages", FieldValue.arrayUnion(messageData(Timestamp.now())))
        .await()

    // Update the last message in the conversation
    conversationDocumentRef.update("lastMessage", messageData(Timestamp.now())).await()
  }
}
