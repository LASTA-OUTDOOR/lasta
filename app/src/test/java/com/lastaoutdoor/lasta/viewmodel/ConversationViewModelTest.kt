package com.lastaoutdoor.lasta.viewmodel

import FakeSocialDB
import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConversationViewModelTest {

  @ExperimentalCoroutinesApi @get:Rule val mainDispatcherRule = MainDispatcherRule()
  private val flow = flowOf(UserPreferences(true))
  private lateinit var viewModel: ConversationViewModel
  private var fakeSocialDB = FakeSocialDB()
  private var fakePreferencesRepository = FakePreferencesRepository(flow)
  private var fakeConnectivityViewRepo = FakeConnectivityviewRepo()

  @Before
  fun setUp() {
    viewModel =
        ConversationViewModel(fakeSocialDB, fakeConnectivityViewRepo, fakePreferencesRepository)
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
}
