package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.useractivities.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import javax.inject.Inject

class SocialDBRepositoryImpl
@Inject
constructor(private val context: Context, private val database: FirebaseFirestore) :
    SocialDBRepository {
  private val userCollection = database.collection(context.getString(R.string.user_db_name))

  override fun getFriends(userId: String): List<UserModel> {
    TODO("Not yet implemented")
  }

  override fun getLatestFriendActivities(userId: String, days: Int): List<ActivitiesDatabaseType> {
    TODO("Not yet implemented")
  }

  override fun getAllConversations(
      userId: String,
      friends: List<UserModel>
  ): List<ConversationModel> {
    TODO("Not yet implemented")
  }

  override fun getConversation(userId: String, friendId: String): ConversationModel {
    TODO("Not yet implemented")
  }

  override fun sendFriendRequest(uid: String, email: String): Boolean {
    TODO("Not yet implemented")
  }

  override fun getFriendRequests(userId: String): List<UserModel> {
    TODO("Not yet implemented")
  }

  override fun acceptFriendRequest(source: String, requester: String) {
    TODO("Not yet implemented")
  }

  override fun declineFriendRequest(source: String, requester: String) {
    TODO("Not yet implemented")
  }

  override fun sendMessage(userId: String, friendUserId: String, message: String) {
    TODO("Not yet implemented")
  }
}
