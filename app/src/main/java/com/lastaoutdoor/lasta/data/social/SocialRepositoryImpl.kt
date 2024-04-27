package com.lastaoutdoor.lasta.data.social

import com.lastaoutdoor.lasta.data.db.DatabaseManager
import com.lastaoutdoor.lasta.models.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.SocialRepository
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

class SocialRepositoryImpl @Inject constructor() : SocialRepository {

  // database manager for firestore interactions
  private val manager = DatabaseManager()

  override fun getFriends(userId: String): List<UserModel> {
    return manager.getFriends(userId)
  }

  override fun getLatestFriendActivities(userId: String, days: Int): List<ActivitiesDatabaseType> {
    return emptyList()
  }

  override fun getAllConversations(
      userId: String,
      friends: List<UserModel>
  ): List<ConversationModel> {
    return manager.getAllConversation(userId, friends)
  }

  override fun getConversation(userId: String, friendId: String): ConversationModel {
    return manager.getConversation(userId, friendId)
  }

  // send a friend request to the user with the given email
  override fun sendFriendRequest(uid: String, email: String): Boolean {

    // Verify if the user exists
    var user: UserModel?
    runBlocking { user = manager.getUserFromEmail(email) }
    if (user == null) {
      return false
    }

    // here we know that the user exists
    manager.addFriendRequest(uid, user!!)
    return true
  }

  // returns all the friend requests
  override fun getFriendRequests(userId: String): List<UserModel> {

    // get the friend requests
    return manager.getFriendRequests(userId)
  }

  // accept a friend request
  override fun acceptFriendRequest(source: String, requester: String) {
    manager.acceptFriendRequest(source, requester)
  }

  // decline a friend request
  override fun declineFriendRequest(source: String, requester: String) {
    manager.declineFriendRequest(source, requester)
  }

  override fun sendMessage(userId: String, friendUserId: String, message: String) {
    manager.sendMessage(userId, friendUserId, message)
  }
}
