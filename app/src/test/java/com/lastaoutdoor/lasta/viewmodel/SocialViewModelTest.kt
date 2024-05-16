package com.lastaoutdoor.lasta.viewmodel

import FakeSocialDB
import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.api.notifications.FCMApi
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.TokenDBRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeTokenDBRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SocialViewModelTest {
  @ExperimentalCoroutinesApi @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private val repoDB = FakeSocialDB()
  private lateinit var userDB: UserDBRepository
  private val connectRepo = FakeConnectivityviewRepo()
  private val context: Context = mockk()
  private lateinit var viewModel: SocialViewModel
  private val prefRepo = FakePreferencesRepository()
  private val tokenDBRepository: TokenDBRepository = FakeTokenDBRepo()
  private val fcmAPI = mockk<FCMApi>(relaxed = true)
  private val errorToast = mockk<ErrorToast>()

  // Mock the user preferences flow

  @ExperimentalCoroutinesApi
  @Before
  fun setUp() {
    userDB = FakeUserDB()
    viewModel =
        SocialViewModel(
            context, repoDB, userDB, connectRepo, prefRepo, tokenDBRepository, fcmAPI, errorToast)
  }

  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

  @ExperimentalCoroutinesApi
  @Test
  fun vm() {
    assertEquals(repoDB.fakeMSG, MessageModel(UserModel("moi"), "toi", Timestamp(0, 0)))
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
    val q = viewModel.friends
    val m = viewModel.messages
    viewModel.friendRequests
    viewModel.latestFriendActivities

    assertEquals(repoDB.fakeMSG, MessageModel(UserModel("moi"), "toi", Timestamp(0, 0)))
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
    val q = viewModel.friends
    val m = viewModel.messages
    viewModel.friendRequests
    viewModel.latestFriendActivities
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `test request friend`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    coEvery { fcmAPI.sendMessage(any()) } returns Unit
    viewModel.requestFriend("test@email.com")
    viewModel.acceptFriend(UserModel("userId"))
    viewModel.declineFriend(UserModel("userId"))
  }

  @Test
  fun `test request friend with invalid email`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    viewModel.requestFriend("test")
  }
}
