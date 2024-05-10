package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivitiesDBRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MoreInfoScreenViewModelTest {
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

  @Test
  fun testFetchDiff() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val moreInfoScreenViewModel: MoreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, fakeUserDB)
    val fakeActivity =
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.EASY)
    val fakeActivity2 =
        Activity("b", 10, activityType = ActivityType.HIKING, difficulty = Difficulty.NORMAL)
    val fakeActivity3 =
        Activity("c", 10, activityType = ActivityType.BIKING, difficulty = Difficulty.HARD)

    moreInfoScreenViewModel.activityToDisplay
    moreInfoScreenViewModel.isMapDisplayed
    moreInfoScreenViewModel.changeActivityToDisplay(fakeActivity)
    moreInfoScreenViewModel.processDiffText(fakeActivity)
  }

  @Test
  fun testMoreInfoMarker() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val moreInfoScreenViewModel: MoreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, fakeUserDB)
    val fakeActivity =
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.EASY)
    val fakeActivity2 =
        Activity("b", 10, activityType = ActivityType.HIKING, difficulty = Difficulty.NORMAL)
    val fakeActivity3 =
        Activity("c", 10, activityType = ActivityType.BIKING, difficulty = Difficulty.HARD)
    assertEquals(
        moreInfoScreenViewModel.goToMarker(fakeActivity),
        Marker(10, "", LatLng(0.0, 0.0), "", R.drawable.climbing_icon, ActivityType.CLIMBING))
    assertEquals(
        moreInfoScreenViewModel.goToMarker(fakeActivity2),
        Marker(10, "", LatLng(0.0, 0.0), "", R.drawable.hiking_icon, ActivityType.HIKING))
    assertEquals(
        moreInfoScreenViewModel.goToMarker(fakeActivity3),
        Marker(10, "", LatLng(0.0, 0.0), "", R.drawable.hiking_icon, ActivityType.BIKING))
  }
}
