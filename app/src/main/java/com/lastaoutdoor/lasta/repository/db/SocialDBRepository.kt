package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel

interface SocialDBRepository {

  // returns all the friends of the user
  suspend fun getFriends(friendIds: List<String>): List<UserModel>

  suspend fun getFriendRequests(userId: String): List<UserModel>

  // returns all the activities done by friends in the last
  suspend fun getLatestFriendActivities(userId: String, days: Int): List<UserActivity>

  // returns all the conversations of the user

  suspend fun getConversation(
      user: UserModel,
      friend: UserModel,
      createNew: Boolean = true
  ): ConversationModel

  suspend fun getAllConversations(userId: String): List<ConversationModel>

  // sends a friend request to the user with the given email, returns true if the request was sent
  // successfully
  suspend fun sendFriendRequest(userId: String, receiverId: String)

  // accept a friend request
  suspend fun acceptFriendRequest(source: String, requester: String)

  // decline a friend request
  suspend fun declineFriendRequest(source: String, requester: String)

  // send a message from a user to a friend
  suspend fun sendMessage(userId: String, friendUserId: String, message: String)
}
