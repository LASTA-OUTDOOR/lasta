package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.models.user.UserPreferences
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class PreferencesViewModelTest {

  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
  private val flow = flowOf(UserPreferences(true))
  val db = FakePreferencesRepository(flow)
  val db2 = FakeUserDB()
  val vm = PreferencesViewModel(db, db2)

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
  fun prefRepo() {
    val userLevel = UserLevel.BEGINNER
    vm.updateBikingLevel(userLevel)
    vm.updateClimbingLevel(userLevel)
    vm.updateHikingLevel(userLevel)
    vm.updateDescription("")

    vm.updateActivityLevels(
        UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER))
    vm.updateFriendRequests(listOf(""))
    vm.updateDownloadedActivities(listOf(""))
    vm.updateUserInfo(UserModel(""))
    vm.updateLanguage(Language.ENGLISH)
    vm.updatePrefActivity(ActivityType.CLIMBING)
    vm.updateFriends(listOf(""))
    vm.updateIsLoggedIn(true)
    vm.clearPreferences()
    vm.prefActivity
    vm.email
    vm.description
    vm.downloadedActivities
    vm.favorites
    vm.friendRequests
    vm.friends
    vm.isLoggedIn
    vm.language
    vm.userId
    vm.user
    vm.userName
    vm.profilePictureUrl
    vm.levels
    vm.flipFavorite("")
  }
}
