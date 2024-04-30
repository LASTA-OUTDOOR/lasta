package com.lastaoutdoor.lasta.ui.screen.social

import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository

// Class mocking the social repository
class FakeSocialRepository : SocialDBRepository {
  override suspend fun getFriends(friendIds: List<String>): List<UserModel> {
    TODO("Not yet implemented")
  }

  override suspend fun getFriendRequests(userId: String): List<UserModel> {
    TODO("Not yet implemented")
  }

  override suspend fun getLatestFriendActivities(userId: String, days: Int): List<UserActivity> {
    TODO("Not yet implemented")
  }

  override suspend fun getConversation(
      userId: String,
      friendId: String,
      createNew: Boolean
  ): ConversationModel {
    TODO("Not yet implemented")
  }

  override suspend fun getAllConversations(userId: String): List<ConversationModel> {
    TODO("Not yet implemented")
  }

  override suspend fun sendFriendRequest(userId: String, receiverId: String): Boolean {
    TODO("Not yet implemented")
  }

  override suspend fun acceptFriendRequest(source: String, requester: String) {
    TODO("Not yet implemented")
  }

  override suspend fun declineFriendRequest(source: String, requester: String) {
    TODO("Not yet implemented")
  }

  override suspend fun sendMessage(userId: String, friendUserId: String, message: String) {
    TODO("Not yet implemented")
  }
}
