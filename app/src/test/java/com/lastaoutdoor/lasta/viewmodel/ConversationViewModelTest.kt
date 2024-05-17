package com.lastaoutdoor.lasta.viewmodel

import FakeSocialDB
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConversationViewModelTest {

  @ExperimentalCoroutinesApi @get:Rule val mainDispatcherRule = MainDispatcherRule()
  private lateinit var viewModel: ConversationViewModel
  private var fakeSocialDB = FakeSocialDB()
  private var fakeUserDB = FakeUserDB()
  private var fakePreferencesRepository = FakePreferencesRepository()
  private var fakeConnectivityViewRepo = FakeConnectivityviewRepo()
  private var errorToast = mockk<ErrorToast>()

  @Before
  fun setUp() {
    viewModel =
        ConversationViewModel(
            fakeUserDB, mockk(), mockk(), fakeSocialDB, fakePreferencesRepository, errorToast)

    every { errorToast.showToast(ErrorType.ERROR_DATABASE) } returns Unit
  }

  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

  @ExperimentalCoroutinesApi
  @Before
  fun setupDispatcher() {
    Dispatchers.setMain(testDispatcher)
  }

  @ExperimentalCoroutinesApi
  @After
  fun tearDownDispatcher() {
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
  }

  @ExperimentalCoroutinesApi
  @Test
  fun conversationViewModel() {
    viewModel.updateConversation()
    viewModel.hideSendMessageDialog()
    viewModel.showSendMessageDialog()
    viewModel.send("message")
    viewModel.updateFriendUserId("friendId")
    assert(viewModel.friendUserId.isNotEmpty())
  }

  @Test
  fun `update conversation with exception`() {
    fakeUserDB.shouldThrowException = true
    fakeSocialDB.shouldThrowException = true
    try {
      viewModel.updateConversation()
    } catch (e: Exception) {
      errorToast.showToast(ErrorType.ERROR_DATABASE)
    }
  }

  @Test
  fun `send message with exception`() {
    fakeUserDB.shouldThrowException = true
    fakeSocialDB.shouldThrowException = true
    try {
      viewModel.send("message")
    } catch (e: Exception) {
      errorToast.showToast(ErrorType.ERROR_DATABASE)
    }
  }
}
