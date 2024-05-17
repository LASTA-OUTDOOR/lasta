import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.FriendsActivities
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import com.lastaoutdoor.lasta.utils.TimeFrame

class FakeSocialDB : SocialDBRepository {
  var shouldThrowException = false
  val fakeUserModel = UserModel(userId = "id")
  val fakeActivity = ClimbingUserActivity()
  val fakeMSG = MessageModel(UserModel("moi"), "toi", Timestamp(0, 0))
  val fakeConfModel =
      ConversationModel(listOf(UserModel("moi"), UserModel("toi")), listOf(fakeMSG), fakeMSG)

  override suspend fun getFriends(friendIds: List<String>): List<UserModel> {
      if (shouldThrowException) {
        throw Exception("FakeSocialDB: getFriends failed")
      }
        return listOf(fakeUserModel)
  }

  override suspend fun getFriendRequests(userId: String): List<UserModel> {
      if (shouldThrowException) {
        throw Exception("FakeSocialDB: getFriendRequests failed")
      }
        return listOf(fakeUserModel)
  }

  override suspend fun getLatestFriendActivities(
      userId: String,
      timeFrame: TimeFrame,
      friends: List<UserModel>
  ): List<FriendsActivities> {
      if (shouldThrowException) {
        throw Exception("FakeSocialDB: getLatestFriendActivities failed")
      }
      return listOf(FriendsActivities(fakeUserModel, fakeActivity, Activity("1", 1)))
  }

  override suspend fun getConversation(
      user: UserModel,
      friend: UserModel,
      createNew: Boolean
  ): ConversationModel {
        if (shouldThrowException) {
            throw Exception("FakeSocialDB: getConversation failed")
        }
        return fakeConfModel
  }

  override suspend fun getAllConversations(userId: String): List<ConversationModel> {
      if (shouldThrowException) {
                throw Exception("FakeSocialDB: getAllConversations failed")
            }
      return listOf(fakeConfModel)
  }

  override suspend fun sendFriendRequest(userId: String, receiverId: String) {
        if (shouldThrowException) {
                    throw Exception("FakeSocialDB: sendFriendRequest failed")
                }
  }

  override suspend fun acceptFriendRequest(source: String, requester: String) {
        if (shouldThrowException) {
                    throw Exception("FakeSocialDB: acceptFriendRequest failed")
                }
  }

  override suspend fun declineFriendRequest(source: String, requester: String) {
        if (shouldThrowException) {
                    throw Exception("FakeSocialDB: declineFriendRequest failed")
                }
  }

  override suspend fun sendMessage(userId: String, friendUserId: String, message: String) {
        if (shouldThrowException) {
                    throw Exception("FakeSocialDB: sendMessage failed")
                }
  }
}
