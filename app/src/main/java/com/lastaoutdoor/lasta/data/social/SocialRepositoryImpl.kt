package com.lastaoutdoor.lasta.data.social

import com.google.firebase.firestore.FirebaseFirestore
import com.lastaoutdoor.lasta.data.db.DatabaseManager
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.data.preferences.PreferencesRepositoryImpl
import com.lastaoutdoor.lasta.repository.SocialRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SocialRepositoryImpl
@Inject constructor() : SocialRepository {

  // database manager for firestore interactions
  private val manager = DatabaseManager()

  override fun getFriends(): List<UserModel>? {
    return null
  }

  override fun getLatestFriendActivities(days: Int): List<ActivitiesDatabaseType>? {
    return null
  }

  override fun getMessages(): List<String>? {
    return null
  }

  // send a friend request to the user with the given email
  override fun sendFriendRequest(uid: String, email: String): Boolean {

    //Verify if the user exists
    var user: UserModel?
    runBlocking {
      user = manager.getUserFromEmail(email)
    }
    if (user == null) {
      return false
    }
    // here we know that the user exists
    return true
  }


}
