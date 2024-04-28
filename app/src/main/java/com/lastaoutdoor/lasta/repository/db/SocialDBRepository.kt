package com.lastaoutdoor.lasta.repository.db

import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.useractivities.ActivitiesDatabaseType

interface SocialDBRepository {

  // returns all the friends of the user
  fun getFriends(userId: String): List<UserModel>

  // returns all the activities done by friends in the last
  fun getLatestFriendActivities(userId: String, days: Int): List<ActivitiesDatabaseType>

  // returns all the conversations of the user
  fun getAllConversations(userId: String, friends: List<UserModel>): List<ConversationModel>

  fun getConversation(userId: String, friendId: String): ConversationModel

  // sends a friend request to the user with the given email, returns true if the request was sent
  // successfully
  fun sendFriendRequest(uid: String, email: String): Boolean

  // returns all the friend requests
  fun getFriendRequests(userId: String): List<UserModel>

  // accept a friend request
  fun acceptFriendRequest(source: String, requester: String)

  // decline a friend request
  fun declineFriendRequest(source: String, requester: String)

  // send a message from a user to a friend
  fun sendMessage(userId: String, friendUserId: String, message: String)
}
