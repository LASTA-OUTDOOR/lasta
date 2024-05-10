package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.viewModelScope
import com.lastaoutdoor.lasta.data.offline.ActivityDatabaseImpl
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.repository.offline.ActivityDao
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivitiesDBRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import io.mockk.coEvery
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
  private val loc = ActivityDatabaseImpl(dao)
  private val con = FakeConnectivityviewRepo()

  @Before
  fun setUp() {
    coEvery { dao.getAllActivities() } returns listOf(Activity("id", 10))
  }

  @Test
  fun testFavoritesScreenViewModel_OnlineemptyFavorites() {
    runBlocking {
      val con = FakeConnectivityviewRepo()
      favoritesScreenViewModel =
          FavoritesScreenViewModel(preferencesRepository, activitiesRepo, loc, con)

      assert(favoritesScreenViewModel.favorites.value.isEmpty())
      favoritesScreenViewModel.viewModelScope.cancel("")
    }
  }

  @Test
  fun testFavoritesScreenViewModel_emptyFavorites() {
    runBlocking {
      val con = FakeConnectivityviewRepo()
      con.connectionState = flowOf(ConnectionState.OFFLINE)
      favoritesScreenViewModel =
          FavoritesScreenViewModel(preferencesRepository, activitiesRepo, loc, con)
      val f =
          FavoritesScreenViewModel::javaClass.get(favoritesScreenViewModel)
              .getDeclaredMethod("fetchOfflineFavorites")
      f.isAccessible = true
      f.invoke(favoritesScreenViewModel)
      assert(favoritesScreenViewModel.favorites.value.isNotEmpty())
      favoritesScreenViewModel.viewModelScope.cancel("")
    }
  }
}
