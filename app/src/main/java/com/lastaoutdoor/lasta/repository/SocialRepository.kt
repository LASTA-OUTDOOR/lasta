package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel

interface SocialRepository {

  // returns all the friends of the userS
  fun getFriends(userId: String): List<UserModel>

  // returns all the activities done by friends in the last
  fun getLatestFriendActivities(userId: String ,days: Int): List<ActivitiesDatabaseType>

  // returns all the messages
  fun getMessages(userId: String): List<String>

  // sends a friend request to the user with the given email, returns true if the request was sent
  // successfully
  fun sendFriendRequest(uid: String, email: String): Boolean

  // returns all the friend requests
  fun getFriendRequests(userId: String): List<UserModel>

  // accept a friend request
  fun acceptFriendRequest(source: String, requester: String)

  // decline a friend request
  fun declineFriendRequest(source: String, requester: String)
}
