package com.lastaoutdoor.lasta.data.social

import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.SocialRepository
import javax.inject.Inject

class SocialRepositoryImpl @Inject constructor() : SocialRepository {
  override fun getFriends(): List<UserModel>? {
    return null
  }

  override fun getLatestFriendActivities(days: Int): List<ActivitiesDatabaseType>? {
    return null
  }

  override fun getMessages(): List<String>? {
    return null
  }

  override fun sendFriendRequest(email: String): Boolean {
    return false
  }
}
