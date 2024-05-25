package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.offline.ActivityDatabaseImpl
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.repository.offline.ActivityDao
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import com.lastaoutdoor.lasta.utils.Response
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivitiesDBRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityDatabaseImpl
import com.lastaoutdoor.lasta.viewmodel.repo.FakeActivityRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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

  private val errorToast = mockk<ErrorToast>()

  @ExperimentalCoroutinesApi
  @Before
  fun setupDispatcher() {
    Dispatchers.setMain(testDispatcher)

    every { errorToast.showToast(any()) } returns Unit
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
        MoreInfoScreenViewModel(fakeDb, fk, ActivityDatabaseImpl(mockk()), fakeUserDB, errorToast)
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
        MoreInfoScreenViewModel(fakeDb, fk, ActivityDatabaseImpl(mockk()), fakeUserDB, errorToast)
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
        Marker(10, "", LatLng(0.0, 0.0), "", R.drawable.biking_icon, ActivityType.BIKING))
  }

  @Test
  fun testChangeActivityToDisplayByID() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, ActivityDatabaseImpl(mockk()), fakeUserDB, errorToast)
    val fakeActivity =
        Activity("id", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.EASY)
    moreInfoScreenViewModel.changeActivityToDisplayByID("id")
    assertEquals(moreInfoScreenViewModel.activityToDisplay.value!!.activityId, "id")
  }

  @Test
  fun testChangeActivityToDisplayByID_with_exception() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, ActivityDatabaseImpl(mockk()), fakeUserDB, errorToast)
    fakeDb.currResponse = Response.Failure(Exception())
    fk.shouldThrowException = true
    try {
      moreInfoScreenViewModel.changeActivityToDisplayByID("uwhd")
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
    }
    fk.shouldThrowException = false
  }

  @Test
  fun testGetUserModels() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    fakeUserDB.updateUser(UserModel("1"))
    fakeUserDB.updateUser(UserModel("2"))
    fakeUserDB.updateUser(UserModel("3"))
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, ActivityDatabaseImpl(mockk()), fakeUserDB, errorToast)
    moreInfoScreenViewModel.getUserModels(listOf("1", "2", "3"))
    assertEquals(moreInfoScreenViewModel.usersList.value.size, 3)
    assertEquals(moreInfoScreenViewModel.usersList.value[0]!!.userId, "1")
    assertEquals(moreInfoScreenViewModel.usersList.value[1]!!.userId, "2")
    assertEquals(moreInfoScreenViewModel.usersList.value[2]!!.userId, "3")
  }

  @Test
  fun testWriteNewRating() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    fakeUserDB.updateUser(UserModel("1"))
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, ActivityDatabaseImpl(mockk()), fakeUserDB, errorToast)
    moreInfoScreenViewModel.writeNewRating("a", Rating("1", "genial", "5"), "5")
    assertEquals(moreInfoScreenViewModel.ratings.value.size, 1)
    assertEquals(moreInfoScreenViewModel.ratings.value[0].userId, "1")
    assertEquals(moreInfoScreenViewModel.ratings.value[0].comment, "genial")
    assertEquals(moreInfoScreenViewModel.ratings.value[0].rating, "5")
  }

  /* Test update difficulty */
  @Test
  fun testUpdateDifficulty() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val dao = mockk<ActivityDao>()
    val fakeActivityDaoImpl = FakeActivityDatabaseImpl(dao)
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, fakeActivityDaoImpl, fakeUserDB, errorToast)
    val fakeActivity =
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.EASY)
    moreInfoScreenViewModel.updateDifficulty(fakeActivity.activityId)
    assertEquals(fakeActivity.difficulty, Difficulty.EASY)
    val fakeActivity2 =
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.NORMAL)
    moreInfoScreenViewModel.updateDifficulty(fakeActivity2.activityId)
    assertEquals(fakeActivity2.difficulty, Difficulty.NORMAL)
    val fakeActivity3 =
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.HARD)
    moreInfoScreenViewModel.updateDifficulty(fakeActivity3.activityId)
    assertEquals(fakeActivity3.difficulty, Difficulty.HARD)
  }

  // Update difficulty with exception throws correctly
  @Test
  fun testUpdateDifficulty_with_exception() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val dao = mockk<ActivityDao>()
    val fakeActivityDaoImpl = FakeActivityDatabaseImpl(dao)
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, fakeActivityDaoImpl, fakeUserDB, errorToast)
    fakeActivityDaoImpl.shouldThrowException = true
    try {
      moreInfoScreenViewModel.updateDifficulty("a")
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_DAO) }
    }
    fakeActivityDaoImpl.shouldThrowException = false
  }

  @Test
  fun `changeActivityToDisplay with exception`() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, ActivityDatabaseImpl(mockk()), fakeUserDB, errorToast)
    val fakeActivity =
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.EASY)
    fakeDb.currResponse = Response.Success(null)
    fk.shouldThrowException = true
    try {
      moreInfoScreenViewModel.changeActivityToDisplay(fakeActivity)
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
    }

    fakeDb.currResponse = Response.Failure(Exception())
    try {
      moreInfoScreenViewModel.changeActivityToDisplay(fakeActivity)
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_OSM_API) }
    }
    fk.shouldThrowException = false
  }

  @Test
  fun `downloadActivity with exception`() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val dao = mockk<ActivityDao>()
    val fakeActivityDaoImpl = FakeActivityDatabaseImpl(dao)
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, fakeActivityDaoImpl, fakeUserDB, errorToast)
    val fakeActivity =
        Activity("a", 10, activityType = ActivityType.CLIMBING, difficulty = Difficulty.EASY)
    fakeActivityDaoImpl.shouldThrowException = true
    try {
      moreInfoScreenViewModel.downloadActivity(fakeActivity)
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_DAO) }
    }
    fk.shouldThrowException = false
  }

  @Test
  fun `getUserModels with exception`() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val dao = mockk<ActivityDao>()
    val fakeActivityDaoImpl = FakeActivityDatabaseImpl(dao)
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, fakeActivityDaoImpl, fakeUserDB, errorToast)
    fakeUserDB.shouldThrowException = true
    try {
      moreInfoScreenViewModel.getUserModels(listOf("1", "2", "3"))
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
    }
    fk.shouldThrowException = false
  }

  @Test
  fun `writeNewRating with exception`() {
    val fakeDb = FakeActivityRepository()
    val fk = FakeActivitiesDBRepository()
    val fakeUserDB = FakeUserDB()
    val dao = mockk<ActivityDao>()
    val fakeActivityDaoImpl = FakeActivityDatabaseImpl(dao)
    val moreInfoScreenViewModel =
        MoreInfoScreenViewModel(fakeDb, fk, fakeActivityDaoImpl, fakeUserDB, errorToast)
    fk.shouldThrowException = true
    try {
      moreInfoScreenViewModel.writeNewRating("a", Rating("1", "genial", "5"), "5")
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
    }
    fk.shouldThrowException = false
  }
}
