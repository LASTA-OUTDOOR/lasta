package com.lastaoutdoor.lasta.data.social

import android.content.Context
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.repository.SocialRepository

class SocialRepositoryImpl(private val context: Context) : SocialRepository {
  override fun getFriends(): List<UserModel>? {
    return null
  }

  override fun getLatestFriendActivities(days: Int): List<ActivitiesDatabaseType>? {
    return null
  }

  override var isConnected: Boolean = false
}
