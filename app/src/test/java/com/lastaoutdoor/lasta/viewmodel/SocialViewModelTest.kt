package com.lastaoutdoor.lasta.viewmodel

import FakeSocialDB
import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.Timestamp
import com.lastaoutdoor.lasta.data.api.notifications.FCMApi
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.db.SocialDBRepository
import com.lastaoutdoor.lasta.repository.db.TokenDBRepository
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeTokenDBRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.mockk.coEvery
import io.mockk.coVerify
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
  private var userDB = FakeUserDB()
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
    viewModel =
        SocialViewModel(
            context, repoDB, userDB, connectRepo, prefRepo, tokenDBRepository, fcmAPI, errorToast)

    every { errorToast.showToast(any()) } returns Unit
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

    every { context.getString(any()) } returns "stringResource"

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
    userDB.shouldThrowException = false
    repoDB.shouldThrowException = false
    viewModel.requestFriend("test@email.com")
    viewModel.acceptFriend(UserModel("userId"))
    viewModel.declineFriend(UserModel("userId"))
  }

  @Test
  fun `test request friend with invalid email`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    viewModel.requestFriend("test")
  }

  @Test
  fun `test request friend with exception`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    coEvery { fcmAPI.sendMessage(any()) } throws Exception()
    userDB.shouldThrowException = true
    repoDB.shouldThrowException = true
    try {
      viewModel.requestFriend("test@email.com")
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
  }

  @Test
  fun `test request friend with exception 2`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    coEvery { fcmAPI.sendMessage(any()) } throws Exception()
    userDB.shouldThrowException = true
    repoDB.shouldThrowException = true
    try {
      viewModel.acceptFriend(UserModel("userId"))
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
  }

  @Test
  fun `test accept friend with exception`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    coEvery { fcmAPI.sendMessage(any()) } throws Exception()
    userDB.shouldThrowException = true
    repoDB.shouldThrowException = true
    try {
      viewModel.acceptFriend(UserModel("userId"))
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
  }

  @Test
  fun `test decline friend with exception`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    coEvery { fcmAPI.sendMessage(any()) } throws Exception()
    userDB.shouldThrowException = true
    repoDB.shouldThrowException = true
    try {
      viewModel.declineFriend(UserModel("userId"))
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
  }

  @Test
  fun `test refresh friends with exception`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    coEvery { fcmAPI.sendMessage(any()) } throws Exception()
    userDB.shouldThrowException = true
    repoDB.shouldThrowException = true
    try {
      viewModel.refreshFriends()
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
    userDB.shouldThrowException = false
    repoDB.shouldThrowException = false
  }

  @Test
  fun `test refresh messages with exception`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    coEvery { fcmAPI.sendMessage(any()) } throws Exception()
    userDB.shouldThrowException = true
    repoDB.shouldThrowException = true
    try {
      viewModel.refreshMessages()
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
    userDB.shouldThrowException = false
    repoDB.shouldThrowException = false
  }

  @Test
  fun `test refresh friend requests with exception`() = runTest {
    every { context.getString(any()) } returns "stringResource"
    coEvery { fcmAPI.sendMessage(any()) } throws Exception()
    userDB.shouldThrowException = true
    repoDB.shouldThrowException = true
    try {
      viewModel.refreshFriendRequests()
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
    userDB.shouldThrowException = false
    repoDB.shouldThrowException = false
  }

  @Test
  fun `test refresh friend requests with exception 2`() = runTest {
    val actualSocialDB = mockk<SocialDBRepository>()
    coEvery { actualSocialDB.getFriendRequests(any()) } throws Exception()
    try {
      SocialViewModel(
          context,
          actualSocialDB,
          userDB,
          connectRepo,
          prefRepo,
          tokenDBRepository,
          fcmAPI,
          errorToast)
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
  }

  @Test
  fun `test refreshFriends with exception`() = runTest {
    val actualSocialDB = mockk<SocialDBRepository>()
    coEvery { actualSocialDB.getFriends(any()) } throws Exception()
    try {
      SocialViewModel(
          context,
          actualSocialDB,
          userDB,
          connectRepo,
          prefRepo,
          tokenDBRepository,
          fcmAPI,
          errorToast)
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
  }

  @Test
  fun `test refreshMessages with exception`() = runTest {
    val actualSocialDB = mockk<SocialDBRepository>()
    coEvery { actualSocialDB.getAllConversations(any()) } throws Exception()
    try {
      SocialViewModel(
          context,
          actualSocialDB,
          userDB,
          connectRepo,
          prefRepo,
          tokenDBRepository,
          fcmAPI,
          errorToast)
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
  }

  @Test
  fun `test refreshFriendRequests with exception`() = runTest {
    val actualSocialDB = mockk<SocialDBRepository>()
    coEvery { actualSocialDB.getLatestFriendActivities(any(), any(), any()) } throws Exception()
    try {
      SocialViewModel(
          context,
          actualSocialDB,
          userDB,
          connectRepo,
          prefRepo,
          tokenDBRepository,
          fcmAPI,
          errorToast)
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
  }
}
