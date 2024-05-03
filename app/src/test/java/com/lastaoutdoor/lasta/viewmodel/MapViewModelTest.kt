package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

class MapViewModelTest {
  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
  private val flow = flowOf(UserPreferences(true))
  val db = FakeActivityRepository()

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
}
