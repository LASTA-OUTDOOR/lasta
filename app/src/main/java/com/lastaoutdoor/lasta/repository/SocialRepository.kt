package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel

interface SocialRepository {

  // returns all the friends of the userS
  fun getFriends(): List<UserModel>?

  // returns all the activities done by friends in the last
  fun getLatestFriendActivities(days: Int): List<ActivitiesDatabaseType>?
  var isConnected: Boolean
}
