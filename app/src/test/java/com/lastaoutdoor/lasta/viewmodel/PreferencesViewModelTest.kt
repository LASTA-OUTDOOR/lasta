package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.utils.ErrorType
import com.lastaoutdoor.lasta.viewmodel.repo.FakePreferencesRepository
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class PreferencesViewModelTest {

  private val errorToast = mockk<ErrorToast>()

  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
  val db = FakePreferencesRepository()
  val db2 = FakeUserDB()
  val vm = PreferencesViewModel(db, db2, errorToast)

  @ExperimentalCoroutinesApi
  @Before
  fun setupDispatcher() {
    Dispatchers.setMain(testDispatcher)

    every { errorToast.showToast(ErrorType.ERROR_DATABASE) } returns Unit

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

  @Test
  fun `update description offline`() {
    db2.shouldThrowException = true
    try {
      vm.updateDescription("")
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
    }
  }

  @Test
  fun `update language offline`() {
    db2.shouldThrowException = true
    try {
      vm.updateLanguage(Language.ENGLISH)
    } catch (e: Exception) {
      coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
    }
  }

  @Test
    fun `update pref activity offline`() {
        db2.shouldThrowException = true
        try {
        vm.updatePrefActivity(ActivityType.CLIMBING)
        } catch (e: Exception) {
        coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
        }
    }

    @Test
    fun `update activity levels offline`() {
        db2.shouldThrowException = true
        try {
        vm.updateActivityLevels(UserActivitiesLevel(UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER))
        } catch (e: Exception) {
        coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
        }
    }

    @Test
    fun `update climbing level offline`() {
        db2.shouldThrowException = true
        try {
        vm.updateClimbingLevel(UserLevel.BEGINNER)
        } catch (e: Exception) {
        coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
        }
    }

    @Test
    fun `update hiking level offline`() {
        db2.shouldThrowException = true
        try {
        vm.updateHikingLevel(UserLevel.BEGINNER)
        } catch (e: Exception) {
        coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
        }
    }

    @Test
    fun `update biking level offline`() {
        db2.shouldThrowException = true
        try {
        vm.updateBikingLevel(UserLevel.BEGINNER)
        } catch (e: Exception) {
        coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
        }
    }

    @Test
    fun `flip favorite offline`() {
        db2.shouldThrowException = true
        vm.favorites = MutableStateFlow(listOf("1"))
        try {
        vm.flipFavorite("1")
        } catch (e: Exception) {
        coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
        }

        vm.favorites = MutableStateFlow(listOf())
        try {
        vm.flipFavorite("1")
        } catch (e: Exception) {
        coVerify { errorToast.showToast(ErrorType.ERROR_DATABASE) }
        }
    }


}
