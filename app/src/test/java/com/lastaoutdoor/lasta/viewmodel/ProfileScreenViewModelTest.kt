package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserActivityRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.grpc.internal.TimeProvider
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


class ProfileScreenViewModelTest {
  @ExperimentalCoroutinesApi @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val flow = flowOf(UserPreferences(true))
  private lateinit var viewModel: ProfileScreenViewModel
  private val tm : com.lastaoutdoor.lasta.data.time.TimeProvider = mockk()
  private val userDb = FakeUserDB()
  private val prefDB = FakePreferencesRepository(flow)
  private val userActDb = FakeUserActivityRepo()
  @Before
  fun setUp(){
      viewModel = ProfileScreenViewModel(userActDb,timeProvider = tm, userDBRepo = userDb, preferences =prefDB )
  }
  @ExperimentalCoroutinesApi
  @Test
  fun profileScreen(){
    viewModel.updateUser("")

    assertEquals(viewModel.user.value, UserModel(""))
  }
}
