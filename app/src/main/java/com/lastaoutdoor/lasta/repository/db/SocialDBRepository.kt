package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel

/**
 * This interface provides methods for interacting with social data.
 */
interface SocialDBRepository {

  /**
   * Returns the list of the user friends
   *
   * @param friendIds the IDs of the friends
   * @return a list of user models representing all the friends
   */
  suspend fun getFriends(friendIds: List<String>): List<UserModel>

  /**
   * Returns the list of the user friend requests
   *
   * @param userId the ID of the user
   * @return a list of user models representing all the friend requests from the users
   */
  suspend fun getFriendRequests(userId: String): List<UserModel>

  /**
   * Gets all the friend activities of the user with the given ID.
   *
   * @param userId the ID of the user
   * @param days the number of days to look back
   */
  suspend fun getLatestFriendActivities(userId: String, days: Int): List<UserActivity>


  /**
   * Returns the conversation between the user with the given ID and the friend with the given ID.
   *
   * @param userId the ID of the user
   * @param friendId the ID of the friend
   * @param createNew whether to create a new conversation if it doesn't exist
   */
  suspend fun getConversation(
      userId: String,
      friendId: String,
      createNew: Boolean = true
  ): ConversationModel

  /**
   * Returns all the conversations of the user with the given ID.
   *
   * @param userId the ID of the user
   * @return a list of conversation models representing all the conversations
   */
  suspend fun getAllConversations(userId: String): List<ConversationModel>

  /**
   * Sends a friend request from the user with the given ID to the receiver with the given ID.
   *
   * @param userId the ID of the user
   * @param receiverId the ID of the receiver
   * @return true if the friend request is sent successfully, false otherwise
   */
  suspend fun sendFriendRequest(userId: String, receiverId: String): Boolean

  /**
   * Accepts a friend request from the source with the given ID to the requester with the given ID.
   *
   * @param source the ID of the source
   * @param requester the ID of the requester
   */
  suspend fun acceptFriendRequest(source: String, requester: String)

  /**
   * Declines a friend request from the source with the given ID to the requester with the given ID.
   *
   * @param source the ID of the source
   * @param requester the ID of the requester
   */
  suspend fun declineFriendRequest(source: String, requester: String)

  /**
   * Sends a message from the user with the given ID to the friend with the given ID.
   *
   * @param userId the ID of the user
   * @param friendUserId the ID of the friend
   * @param message the message to send
   */
  suspend fun sendMessage(userId: String, friendUserId: String, message: String)
}
