package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class SocialDBRepositoryImpl
@Inject
constructor(private val context: Context, private val database: FirebaseFirestore) :
    SocialDBRepository {
  private val userCollection = database.collection(context.getString(R.string.user_db_name))
  private val conversationCollection =
      database.collection(context.getString(R.string.conversations_db_name))

  override suspend fun getFriends(friendIds: List<String>): List<UserModel> {
    val friends: ArrayList<UserModel> = ArrayList()
    val friendsQuery = userCollection.whereIn(FieldPath.documentId(), friendIds).get().await()
    if (!friendsQuery.isEmpty) {
      for (friend in friendsQuery.documents) {
        val friendModel = friend.toObject(UserModel::class.java)
        if (friendModel != null) {
          friends.add(friendModel)
        }
      }
    }

    return friends
  }

  override suspend fun getLatestFriendActivities(userId: String, days: Int): List<UserActivity> {
    TODO("Not yet implemented")
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
    var document = conversationDocumentRef.get().await()
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

  override suspend fun sendFriendRequest(userId: String, receiverId: String) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = userCollection.document(receiverId)

    // Store the uid of the sender in the user's document in an array in case there are multiple
    // requests (This does not allow duplicates)
    userDocumentRef.update("friendRequests", FieldValue.arrayUnion(userId))
  }

  override suspend fun acceptFriendRequest(source: String, requester: String) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = userCollection.document(source)

    // Remove the requester from the friend requests array
    userDocumentRef.update("friendRequests", FieldValue.arrayRemove(requester))

    // Add the requester to the friends array
    userDocumentRef.update("friends", FieldValue.arrayUnion(requester))

    // Create a reference to the requester's document in the Firestore database
    val requesterDocumentRef = userCollection.document(requester)

    // Add the source to the requester's friends array
    requesterDocumentRef.update("friends", FieldValue.arrayUnion(source))
  }

  override suspend fun declineFriendRequest(source: String, requester: String) {
    // Create a reference to the user's document in the Firestore database
    val userDocumentRef = userCollection.document(source)

    // Remove the requester from the friend requests array
    userDocumentRef.update("friendRequests", FieldValue.arrayRemove(requester))
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
    conversationDocumentRef.update("messages", FieldValue.arrayUnion(messageData(Timestamp.now())))

    // Update the last message in the conversation
    conversationDocumentRef.update("lastMessage", messageData(Timestamp.now()))
  }
}
