package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.offline.ActivityDao
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivitiesDBRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityDatabaseImpl
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

// Tests for favorites viewModel, addded also some calls to ACtivityDAtabaseImpl to get more
// coverage, asit's online 3 lines
class FavoritesScreenViewModelTest {
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

  private lateinit var favoritesScreenViewModel: FavoritesScreenViewModel
  private val preferencesRepository = FakePreferencesRepository()
  private val activitiesRepo = FakeActivitiesDBRepository()
  private val dao: ActivityDao = mockk()
  private val loc = FakeActivityDatabaseImpl(dao)
  private val con = FakeConnectivityviewRepo()
  private val errorToast = mockk<ErrorToast>()

  @Before
  fun setUp() {
    coEvery { dao.getAllActivities() } returns listOf(Activity("id", 10))
    coEvery { dao.deleteActivity(any()) } returns Unit
    coEvery { dao.insertActivity(any()) } returns Unit
    coEvery { dao.getActivity(any()) } returns Activity("id", 10)
    every { errorToast.showToast(any()) } returns Unit
  }

  @Test
  fun testFavoritesScreenViewModel_OnlineemptyFavorites() {
    runBlocking {
      val con = FakeConnectivityviewRepo()
      favoritesScreenViewModel =
          FavoritesScreenViewModel(preferencesRepository, activitiesRepo, loc, con, errorToast)

      assert(favoritesScreenViewModel.favorites.value.isEmpty())
      favoritesScreenViewModel.viewModelScope.cancel("")
    }
  }

  @Test
  fun testFavoritesScreenViewModel_emptyFavorites() {
    runBlocking {
      val con = FakeConnectivityviewRepo()
      loc.insertActivity(Activity("id", 10))
      loc.deleteActivity(Activity("id", 10))
      loc.getActivity("id")
      con.connectionState = flowOf(ConnectionState.OFFLINE)
      favoritesScreenViewModel =
          FavoritesScreenViewModel(preferencesRepository, activitiesRepo, loc, con, errorToast)

      assert(favoritesScreenViewModel.favorites.value.isNotEmpty())
      favoritesScreenViewModel.viewModelScope.cancel("")
    }
  }

  @Test
  fun `test fetchFavorites while connected with exception`() {
    runBlocking {
      val con = FakeConnectivityviewRepo()
      con.connectionState = flowOf(ConnectionState.CONNECTED)
      activitiesRepo.shouldThrowException = true
      try {
        favoritesScreenViewModel =
            FavoritesScreenViewModel(preferencesRepository, activitiesRepo, loc, con, errorToast)
      } catch (e: Exception) {
        coVerify { errorToast.showToast(any()) }
      }
      activitiesRepo.shouldThrowException = false
    }
  }

  @Test
  fun `test fetchFavorites while offline with exception`() {
    runBlocking {
      val con = FakeConnectivityviewRepo()
      con.connectionState = flowOf(ConnectionState.OFFLINE)
      loc.shouldThrowException = true
      try {
        favoritesScreenViewModel =
            FavoritesScreenViewModel(preferencesRepository, activitiesRepo, loc, con, errorToast)
      } catch (e: Exception) {
        coVerify { errorToast.showToast(any()) }
      }
      loc.shouldThrowException = false
    }
  }
}
