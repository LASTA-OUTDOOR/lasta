package com.lastaoutdoor.lasta.data.db

import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.MockTask
import com.lastaoutdoor.lasta.data.time.TimeProvider
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivitiesDBRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserActivityRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(manifest = Config.NONE)
class SocialDBRepoTest {
  private val firestore: FirebaseFirestore = mockk(relaxed = true)
  private val context: Context = mockk()
  private val user: FirebaseUser = mockk()
  private lateinit var expectedUser: UserModel
  private lateinit var expectedUser2: UserModel
  private lateinit var expectedConversation: ConversationModel
  private val friendsIds = listOf("friend")
  private val documentReference: DocumentReference = mockk()
  private val collectionReference: CollectionReference = mockk()
  private val query: Query = mockk()
  private val query2: Query = mockk()

  private val querySN: QuerySnapshot = mockk()

  private val mockTimeProvider = mockk<TimeProvider>()
  private val userActDb = FakeUserActivityRepo()
  private val actDb = FakeActivitiesDBRepository()

  private val documentSnapshot: DocumentSnapshot = mockk()
  private lateinit var activitiesRepository: SocialDBRepositoryImpl
  private val task: MockTask<QuerySnapshot> = mockk()
  private val task2: MockTask<Void> = mockk()
  private val task4: MockTask<Void> = mockk()

  private val task3: MockTask<DocumentSnapshot> = mockk()

  private val l = listOf(documentSnapshot)

  @Before
  fun setup() {

    every { context.getString(R.string.user_db_name) } returns "users"
    every { context.getString(R.string.conversations_db_name) } returns "conversations"

    // every { firestore.collection(any()) } returns mockk()
    every { firestore.collection("users") } returns collectionReference
    every { firestore.collection("conversations") } returns collectionReference
    every { collectionReference.document("frienduserId") } returns documentReference

    every { collectionReference.whereIn(FieldPath.documentId(), friendsIds) } returns query
    every { collectionReference.document("userId") } returns documentReference

    every { documentSnapshot.get("messages") } returns
        arrayListOf(
            hashMapOf<String, Any>(
                "from" to "friend", "content" to "coucou", "timestamp" to Timestamp(0, 0)))
    every { user.getUid() } returns "userId"
    every { querySN.documents } returns l
    coEvery { documentReference.get() } returns task3
    coEvery { query.get() } returns task
    // coEvery { documentReference.get() } returns task
    every { task.isComplete } returns true
    every { task.isCanceled } returns false
    every { task.isSuccessful } returns true
    every { task.result } returns querySN
    every { task.exception } returns null
    every { task.await() } returns querySN
    every { task2.isComplete } returns true
    every { task2.isCanceled } returns false
    every { task2.isSuccessful } returns true
    every { task2.result } returns mockk()
    every { task2.exception } returns null
    every { task2.await() } returns mockk()
    every { task4.isComplete } returns true
    every { task4.isCanceled } returns false
    every { task4.isSuccessful } returns true
    every { task4.result } returns mockk()
    every { task4.exception } returns null
    every { task4.await() } returns mockk()
    every { task3.isComplete } returns true
    every { task3.isCanceled } returns false
    every { task3.isSuccessful } returns true
    every { task3.result } returns documentSnapshot
    every { task3.exception } returns null
    every { task3.await() } returns documentSnapshot
    expectedUser = UserModel(userId = "friend")

    every { mockTimeProvider.currentDate() } returns LocalDate.of(2022, 4, 15)

    activitiesRepository =
        SocialDBRepositoryImpl(context, firestore, userActDb, mockTimeProvider, actDb)
  }

  @Test
  fun `test getFriends`() = runBlocking {
    every { querySN.isEmpty } returns false

    every { documentSnapshot.id } returns friendsIds[0]
    every { documentSnapshot.getString("userName")!! } returns ""
    every { documentSnapshot.getString("email")!! } returns ""
    every { documentSnapshot.getString("description")!! } returns ""
    every { documentSnapshot.getString("profilePictureUrl")!! } returns ""
    every { documentSnapshot.getString("language")!! } returns Language.ENGLISH.name

    every { documentSnapshot.getString("prefActivity")!! } returns ActivityType.CLIMBING.name

    every { documentSnapshot.get("levels") } returns
        hashMapOf(
            "climbinglevel" to UserLevel.BEGINNER.name,
            "hikinglevel" to UserLevel.BEGINNER.name,
            "bikinglevel" to UserLevel.BEGINNER.name)
    every { documentSnapshot.get("friendRequests") } returns ArrayList<String>()
    every { documentSnapshot.get("friends") } returns ArrayList<String>()
    every { documentSnapshot.get("favorites") } returns ArrayList<String>()
    val f = activitiesRepository.getFriends(friendsIds)
    assertEquals(expectedUser, f[0])
  }

  @Test
  fun `test getFriendsEmpty`() = runBlocking {
    every { querySN.isEmpty } returns true
    val f = activitiesRepository.getFriends(friendsIds)
    assertEquals(emptyList<String>(), f)
  }

  @Test
  fun `test getFriendsParamEmpty`() = runBlocking {
    every { querySN.isEmpty } returns true
    val f = activitiesRepository.getFriends(emptyList<String>())
    val f2 = activitiesRepository.getFriends(listOf(""))
    assertEquals(emptyList<String>(), f)
  }

  @Test
  fun `test getFriendsEmptyLevel`() = runBlocking {
    every { querySN.isEmpty } returns false

    every { documentSnapshot.id } returns friendsIds[0]
    every { documentSnapshot.getString("userName")!! } returns ""
    every { documentSnapshot.getString("email")!! } returns ""
    every { documentSnapshot.getString("description")!! } returns ""
    every { documentSnapshot.getString("profilePictureUrl")!! } returns ""
    every { documentSnapshot.getString("language")!! } returns Language.ENGLISH.name

    every { documentSnapshot.getString("prefActivity")!! } returns ActivityType.CLIMBING.name

    every { documentSnapshot.get("levels") } returns hashMapOf<String, String>()
    every { documentSnapshot.get("friendRequests") } returns ArrayList<String>()
    every { documentSnapshot.get("friends") } returns ArrayList<String>()
    every { documentSnapshot.get("favorites") } returns ArrayList<String>()
    val f = activitiesRepository.getFriends(friendsIds)
    assertEquals(expectedUser, f[0])
  }

  @Test
  fun `test getFriendRequests`() = runBlocking {
    expectedUser2 = UserModel(userId = "friend", friendRequests = listOf("friend"))
    every { querySN.isEmpty } returns false

    every { documentSnapshot.id } returns friendsIds[0]
    every { documentSnapshot.getString("userName")!! } returns ""
    every { documentSnapshot.getString("email")!! } returns ""
    every { documentSnapshot.getString("description")!! } returns ""
    every { documentSnapshot.getString("profilePictureUrl")!! } returns ""
    every { documentSnapshot.getString("language")!! } returns Language.ENGLISH.name

    every { documentSnapshot.getString("prefActivity")!! } returns ActivityType.CLIMBING.name

    every { documentSnapshot.get("levels") } returns
        hashMapOf(
            "climbinglevel" to UserLevel.BEGINNER.name,
            "hikinglevel" to UserLevel.BEGINNER.name,
            "bikinglevel" to UserLevel.BEGINNER.name)
    every { documentSnapshot.get("friendRequests") } returns arrayListOf("friend")
    every { documentSnapshot.get("friends") } returns ArrayList<String>()
    every { documentSnapshot.get("favorites") } returns ArrayList<String>()
    val f = activitiesRepository.getFriendRequests("userId")
    assertEquals(expectedUser2, f[0])
  }

  @Test
  fun `test getConversation`() = runBlocking {
    expectedConversation =
        ConversationModel(
            listOf(UserModel("userId"), UserModel("friend")),
            listOf(
                MessageModel(
                    from = UserModel("friend"), content = "coucou", timestamp = Timestamp(0, 0))),
            MessageModel(
                from = UserModel("friend"), content = "coucou", timestamp = Timestamp(0, 0)))
    every { documentSnapshot.exists() } returns true

    val f = activitiesRepository.getConversation(UserModel("userId"), UserModel("friend"))
    assertEquals(expectedConversation, f)
  }

  @Test
  fun `test getConversationEmpty`() = runBlocking {
    expectedConversation =
        ConversationModel(listOf(UserModel("userId"), UserModel("friend")), emptyList(), null)
    every { documentSnapshot.exists() } returns false
    every { documentReference.set(any(), any()) } returns mockk()
    val f = activitiesRepository.getConversation(UserModel("userId"), UserModel("friend"))

    assertEquals(expectedConversation, f)
    coVerify { documentReference.set(any(), any()) }
  }

  @Test
  fun `test getConversationEmptyandNotCreate`() = runBlocking {
    expectedConversation = ConversationModel(listOf(), emptyList(), null)
    every { documentSnapshot.exists() } returns false

    val f =
        activitiesRepository.getConversation(
            UserModel("userId"), UserModel("friend"), createNew = false)

    assertEquals(expectedConversation, f)
    // coVerify { documentReference.set(any(), any()) }
  }

  @Test
  fun getAllConversation() {
    runBlocking {
      every { documentSnapshot.exists() } returns true
      every { documentSnapshot.getString("userName")!! } returns ""
      every { documentSnapshot.getString("email")!! } returns ""
      every { documentSnapshot.getString("description")!! } returns ""
      every { documentSnapshot.getString("profilePictureUrl")!! } returns ""
      every { documentSnapshot.getString("language")!! } returns Language.ENGLISH.name
      every { documentSnapshot.getString("climbingLevel") } returns UserLevel.BEGINNER.name
      every { documentSnapshot.getString("hikingLevel") } returns UserLevel.BEGINNER.name
      every { documentSnapshot.getString("bikingLevel") } returns UserLevel.BEGINNER.name
      every { documentSnapshot.get("friendRequests") } returns arrayListOf("friend")
      every { documentSnapshot.get("friends") } returns ArrayList<String>()
      every { documentSnapshot.get("favorites") } returns ArrayList<String>()
      every { documentSnapshot.getString("prefActivity")!! } returns ActivityType.CLIMBING.name
      every { collectionReference.document("userIduserId") } returns documentReference
      every { documentSnapshot.get("members") } returns arrayListOf("userId", "frienduserId")
      every { documentSnapshot.getId() } returns "userId"
      every { documentSnapshot.getString("userName") } returns ""
      expectedConversation =
          ConversationModel(
              members =
                  listOf(
                      UserModel(userId = "userId", friendRequests = listOf("friend")),
                      UserModel(userId = "userId", friendRequests = listOf("friend"))),
              messages =
                  listOf(
                      MessageModel(
                          from = UserModel(userId = "userId", friendRequests = listOf("friend")),
                          content = "coucou",
                          timestamp = Timestamp(0, 0))),
              lastMessage =
                  MessageModel(
                      from = UserModel(userId = "userId", friendRequests = listOf("friend")),
                      content = "coucou",
                      timestamp = Timestamp(0, 0)))
      every { collectionReference.whereArrayContains("members", "userId") } returns query
      every { documentSnapshot.toObject(ConversationModel::class.java) } returns
          expectedConversation
      val f = activitiesRepository.getAllConversations("userId")
      assertEquals(f[0], expectedConversation)
    }
  }

  @Test
  fun sendMessage() {
    runBlocking {
      every { documentSnapshot.exists() } returns true
      every { documentReference.update("messages", any()) } returns task2
      every { documentReference.update("lastMessage", any()) } returns task4

      activitiesRepository.sendMessage("userId", "friend", "coucou")
      coVerify { documentReference.update(any() as String, any()) }
    }
  }

  @Test
  fun sendMessageNull() {
    runBlocking {
      // every { documentSnapshot.exists() } returns false

      activitiesRepository.sendMessage("", "friend", "coucou")
    }
  }

  @Test
  fun sendMessageNull2() {
    runBlocking {
      every { documentSnapshot.exists() } returns false
      every { documentReference.update("messages", any()) } returns task2
      every { documentReference.update("lastMessage", any()) } returns task4

      activitiesRepository.sendMessage("userId", "friend", "coucou")
    }
  }

  @Test
  fun AcceptFriendRequest() {
    runBlocking {
      every { documentSnapshot.exists() } returns false
      every { collectionReference.document("friend") } returns documentReference
      every { documentReference.update("friendRequests", any()) } returns task2
      every { documentReference.update("friends", any()) } returns task4

      activitiesRepository.acceptFriendRequest("userId", "friend")
      coVerify { documentReference.update(any() as String, any()) }
    }
  }

  @Test
  fun declineFriendRequest() {
    runBlocking {
      every { documentSnapshot.exists() } returns false
      every { collectionReference.document("friend") } returns documentReference
      every { documentReference.update("friendRequests", any()) } returns task2

      activitiesRepository.declineFriendRequest("userId", "friend")
      coVerify { documentReference.update(any() as String, any()) }
    }
  }

  @Test
  fun sendFriendRequest() {
    runBlocking {
      every { documentSnapshot.exists() } returns false
      every { collectionReference.document("friend") } returns documentReference
      every { documentReference.update("friendRequests", any()) } returns task2

      activitiesRepository.sendFriendRequest("userId", "friend")
      coVerify { documentReference.update(any() as String, any()) }
    }
  }

  /*
  @Test
  fun getLatestFriendActivities() {
    runBlocking{
      val activities = listOf(userActDb.fakeActivity)
      // For example, April 15, 2022 Friday
      every { filterTrailsByTimeFrame(activities, TimeFrame.W, mockTimeProvider) } returns activities
      every { calculateTimeRangeUntilNow(any(), mockTimeProvider) } returns Pair(Timestamp(0, 0), Timestamp(0, 0))
      every { }
      val a = activitiesRepository.getLatestFriendActivities("userId", TimeFrame.W, listOf(UserModel("friends")))
      val expected = listOf(FriendsActivities(UserModel("friends"), userActDb.fakeActivity, Activity("", 3)))
        assertEquals(expected, a)
    }
  }

   */

  @Test
  fun `test deleteAllConversations`() = runTest {
    every { documentSnapshot.exists() } returns true
    every { documentSnapshot.reference } returns documentReference
    every { documentReference.delete() } returns task2
    val queryAnswer = mockk<Query>()
    every { collectionReference.whereArrayContains("members", "userId") } returns queryAnswer
    every { queryAnswer.get() } returns task
    every { task.await() } returns querySN
    val documents = listOf(documentSnapshot)
    every { querySN.documents } returns documents
    activitiesRepository.deleteAllConversations("userId")
    coVerify { documentReference.delete() }
  }
}
