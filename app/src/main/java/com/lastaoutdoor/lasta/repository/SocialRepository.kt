package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel

interface SocialRepository {

  // returns all the friends of the userS
  fun getFriends(): List<UserModel>?

  // returns all the activities done by friends in the last
  fun getLatestFriendActivities(days: Int): List<ActivitiesDatabaseType>?

  // returns the Internet connection status
  var isConnected: Boolean

  // returns all the messages
  fun getMessages(): List<String>?
}
