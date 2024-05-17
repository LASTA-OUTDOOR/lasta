package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.models.user.ClimbingUserActivity
import com.lastaoutdoor.lasta.models.user.UserActivity
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserActivityRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileScreenViewModelTest {

  private val errorToast = mockk<ErrorToast>(relaxed = true)

  @ExperimentalCoroutinesApi @get:Rule val mainDispatcherRule = MainDispatcherRule()
  private lateinit var viewModel: ProfileScreenViewModel
  private val tm: com.lastaoutdoor.lasta.data.time.TimeProvider =
      mockk("TimeProvider", relaxed = true)
  private val userDb = FakeUserDB()
  private val prefDB = FakePreferencesRepository()
  private val userActDb = FakeUserActivityRepo()

  @Before
  fun setUp() {
    viewModel =
        ProfileScreenViewModel(
            userActDb,
            timeProvider = tm,
            userDBRepo = userDb,
            preferences = prefDB,
            errorToast = errorToast)
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
  fun profileScreen() {
    viewModel.updateUser("")
    viewModel.updateUser("2")
    viewModel.setSport(com.lastaoutdoor.lasta.models.activity.ActivityType.BIKING)
    viewModel.activities.value = List(10) { ClimbingUserActivity() }
    viewModel.setTimeFrame(com.lastaoutdoor.lasta.utils.TimeFrame.ALL)
    viewModel.setTimeFrame(com.lastaoutdoor.lasta.utils.TimeFrame.Y)
    viewModel.isCurrentUser.value = false
    viewModel.activities.value = emptyList()
    viewModel.user.value = UserModel("")
    viewModel.addFakeActivity()
    assertEquals(viewModel.user.value, UserModel(""))
    assertEquals(viewModel.isCurrentUser.value, false)
    assertEquals(viewModel.activities.value, emptyList<UserActivity>())
  }

  @Test
  fun `test updateUser with no connection`() = runTest {
    userDb.shouldThrowException = true
    try {
      viewModel.updateUser("id")
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
    }
  }
}
