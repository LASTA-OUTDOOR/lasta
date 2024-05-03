package com.lastaoutdoor.lasta.viewmodel

import FakeSocialDB
import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainCoroutineRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

  override fun starting(description: Description?) {
    super.starting(description)
    Dispatchers.setMain(dispatcher)
  }

  override fun finished(description: Description?) {
    super.finished(description)
    Dispatchers.resetMain()
  }
}

class SocialViewModelTest {
  @ExperimentalCoroutinesApi @get:Rule val mainDispatcherRule = MainDispatcherRule()
  private val flow = flowOf(UserPreferences(true))

  private val repoDB = FakeSocialDB()
  private val userDB = FakeUserDB()
  private val connectRepo = FakeConnectivityviewRepo()
  private val context: Context = mockk()
  private lateinit var viewModel: SocialViewModel
  @OptIn(ExperimentalCoroutinesApi::class) private val prefRepo = FakePreferencesRepository(flow)

  // Mock the user preferences flow

  @ExperimentalCoroutinesApi
  @Before
  fun setUp() {
    viewModel = SocialViewModel(context, repoDB, userDB, connectRepo, prefRepo)
  }

  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

  @ExperimentalCoroutinesApi
  @Test
  fun vm() {
    assertEquals(repoDB.fakeMSG, MessageModel("moi", "toi", Timestamp(0, 0)))
  }

  @ExperimentalCoroutinesApi
  @Test
  fun showTop() {
    val iv: ImageVector = mockk()
    viewModel.showTopButton(iv, {})
    viewModel.requestFriend("")
    viewModel.acceptFriend(UserModel("userId"))
    viewModel.declineFriend(UserModel("userId"))
    viewModel.refreshFriends()
    viewModel.refreshMessages()
    viewModel.refreshFriendRequests()
    viewModel.user = UserModel("userId")
    viewModel.friends = listOf(UserModel("userId"))
    viewModel.friendRequestFeedback = "prout"
    viewModel.friendRequests = listOf(UserModel("userId"))
    viewModel.displayAddFriendDialog = true
    viewModel.displayFriendPicker = true
    // asssert displayFriendPicker
    assertEquals(viewModel.displayFriendPicker, true)
    viewModel.messages = listOf()
    viewModel.user
    viewModel.friendRequestFeedback
    viewModel.displayFriendPicker
    viewModel.displayAddFriendDialog
    viewModel.topButton
    viewModel.topButtonIcon
    viewModel.topButtonOnClick
    viewModel.getNumberOfDays()
    val q = viewModel.friends
    val m = viewModel.messages
    viewModel.friendRequests
    viewModel.latestFriendActivities

    assertEquals(repoDB.fakeMSG, MessageModel("moi", "toi", Timestamp(0, 0)))
  }

  @ExperimentalCoroutinesApi
  @Test
  fun showTop2() {
    Dispatchers.setMain(testDispatcher)
    val iv: ImageVector = mockk()
    viewModel.showTopButton(iv, {})
    viewModel.requestFriend("")
    viewModel.requestFriend("cass@gmail.com")
    viewModel.requestFriend("cass")
    viewModel.requestFriend("userId")
    viewModel.acceptFriend(UserModel("userId"))
    viewModel.declineFriend(UserModel("userId"))
    viewModel.refreshFriends()
    viewModel.refreshMessages()
    viewModel.refreshFriendRequests()
    viewModel.user = UserModel("userId")
    viewModel.friends = listOf(UserModel("userId"))
    viewModel.friendRequestFeedback = "prout"
    viewModel.friendRequests = listOf(UserModel("userId"))
    viewModel.displayAddFriendDialog = true
    viewModel.displayFriendPicker = true
    // asssert displayFriendPicker
    assertEquals(viewModel.displayFriendPicker, true)
    viewModel.messages = listOf()
    viewModel.user
    viewModel.friendRequestFeedback
    viewModel.displayFriendPicker
    viewModel.displayAddFriendDialog
    viewModel.topButton
    viewModel.topButtonIcon
    viewModel.topButtonOnClick
    viewModel.getNumberOfDays()
    val q = viewModel.friends
    val m = viewModel.messages
    viewModel.friendRequests
    viewModel.latestFriendActivities
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
  }
}
