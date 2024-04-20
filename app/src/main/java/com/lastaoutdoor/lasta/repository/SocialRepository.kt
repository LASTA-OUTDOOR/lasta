package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel

interface SocialRepository {

  // returns all the friends of the userS
  fun getFriends(): List<UserModel>?

  // returns all the activities done by friends in the last
  fun getLatestFriendActivities(days: Int): List<ActivitiesDatabaseType>?

  // returns all the messages
  fun getMessages(): List<String>?

  // sends a friend request to the user with the given email, returns true if the request was sent
  // successfully
  fun sendFriendRequest(email: String): Boolean
}
