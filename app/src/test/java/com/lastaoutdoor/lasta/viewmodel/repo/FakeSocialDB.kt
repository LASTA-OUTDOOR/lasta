import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.FriendsActivities
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import com.lastaoutdoor.lasta.utils.TimeFrame

class FakeSocialDB() : SocialDBRepository {
  val fakeUserModel = UserModel(userId = "id")
  val fakeActivity = ClimbingUserActivity()
  val fakeMSG = MessageModel(UserModel("moi"), "toi", Timestamp(0, 0))
  val fakeConfModel =
      ConversationModel(listOf(UserModel("moi"), UserModel("toi")), listOf(fakeMSG), fakeMSG)

  override suspend fun getFriends(friendIds: List<String>): List<UserModel> {
    return listOf(fakeUserModel)
  }

  override suspend fun getFriendRequests(userId: String): List<UserModel> {
    return listOf(fakeUserModel)
  }

  override suspend fun getLatestFriendActivities(
      userId: String,
      timeFrame: TimeFrame,
      friends: List<UserModel>
  ): List<FriendsActivities> {
    return listOf(FriendsActivities(fakeUserModel, fakeActivity, Activity("1", 1)))
  }

  override suspend fun getConversation(
      user: UserModel,
      friend: UserModel,
      createNew: Boolean
  ): ConversationModel {
    return fakeConfModel
  }

  override suspend fun getAllConversations(userId: String): List<ConversationModel> {
    return listOf(fakeConfModel)
  }

  override suspend fun sendFriendRequest(userId: String, receiverId: String) {}

  override suspend fun acceptFriendRequest(source: String, requester: String) {}

  override suspend fun declineFriendRequest(source: String, requester: String) {}

  override suspend fun sendMessage(userId: String, friendUserId: String, message: String) {}
}
