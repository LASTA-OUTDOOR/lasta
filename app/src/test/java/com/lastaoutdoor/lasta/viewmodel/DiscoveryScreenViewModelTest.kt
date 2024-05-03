package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.utils.Response
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityRepository
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule(val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {
  @ExperimentalCoroutinesApi
  override fun starting(description: Description?) = Dispatchers.setMain(dispatcher)

  class DiscoveryScreenViewModelTest()

  override fun finished(description: Description?) = Dispatchers.resetMain()
}

class DiscoveryScreenViewModelTest() {
  @ExperimentalCoroutinesApi @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: DiscoverScreenViewModel
  private var repo = FakeActivityRepository()

  @Before
  fun setUp() {
    repo.currResponse = Response.Success(null)

    viewModel = DiscoverScreenViewModel(repo, mockk())
  }

  @ExperimentalCoroutinesApi
  @Test
  fun fetchBikingActivities() {
    runBlocking {
      viewModel.fetchActivities()
      assertEquals(viewModel.activities.value, emptyList<Activity>())
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun setScreen() {
    runBlocking {
      viewModel.setScreen(DiscoverDisplayType.LIST)
      assertEquals(viewModel.screen.value, DiscoverDisplayType.LIST)
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun setLoc() {
    runBlocking {
      viewModel.setSelectedLocality(Pair("", LatLng(0.0, 0.0)))
      assertEquals(viewModel.selectedLocality.value, Pair("", LatLng(0.0, 0.0)))
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun setRnage() {
    runBlocking {
      viewModel.setRange(0.0)
      assertEquals(viewModel.range.value, 0.0)
    }
  }

  @ExperimentalCoroutinesApi
  @Test
  fun get() {
    runBlocking { assertEquals(viewModel.localities[0].first, "Ecublens") }
  }
}
