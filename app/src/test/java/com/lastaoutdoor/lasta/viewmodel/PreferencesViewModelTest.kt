package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.data.model.user.HikingLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.data.model.user.UserPreferences
import com.lastaoutdoor.lasta.repository.PreferencesRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class PreferencesViewModelTest {

  private lateinit var fakePreferencesRepo: FakePreferencesRepository
  private lateinit var preferencesViewModel: PreferencesViewModel

  @Before
  fun setUp() {
    fakePreferencesRepo = FakePreferencesRepository()
    preferencesViewModel = PreferencesViewModel(fakePreferencesRepo)
  }

  @Test
  fun `updateIsLoggedIn should update the isLoggedIn preference`() = runTest {
    val isLoggedIn = true

    // When
    preferencesViewModel.updateIsLoggedIn(isLoggedIn)

    val collectJob = launch { preferencesViewModel.isLoggedIn.collect { assertTrue(it) } }

    delay(1000) // Wait for 1 second
    collectJob.cancel()
  }

  @Test
  fun `updateUserId should update the userId preference`() = runTest {
    // Given
    val user =
        UserModel(
            "123",
            "John Doe",
            "testemail@gmail.com",
            "https://www.example.com/profile.jpg",
            "I'm a cool guy",
            HikingLevel.BEGINNER)

    val expectedUserId = "123"

    // When
    preferencesViewModel.updateUserInfo(user)

    // Then
    val collectJob = launch {
      preferencesViewModel.userId.collect { assertEquals(it, expectedUserId) }
    }

    delay(1000) // Wait for 1 second
    collectJob.cancel()
  }

  @Test
  fun `updateUserName should update the userName preference`() = runTest {
    // Given
    val user =
        UserModel(
            "123",
            "John Doe",
            "testemail@gmail.com",
            "https://www.example.com/profile.jpg",
            "I'm a cool guy",
            HikingLevel.INTERMEDIATE)

    val expectedUserName = "John Doe"

    // When
    preferencesViewModel.updateUserInfo(user)

    // Then
    val collectJob = launch {
      preferencesViewModel.userName.collect { assertEquals(it, expectedUserName) }
    }

    delay(1000) // Wait for 1 second
    collectJob.cancel()
  }

  @Test
  fun `updateUserEmail should update the email preference`() = runTest {
    // Given
    val user =
        UserModel(
            "123",
            "John Doe",
            "testemail@gmail.com",
            "https://www.example.com/profile.jpg",
            "I'm a cool guy",
            HikingLevel.ADVANCED)

    val expectedEmail = "testemail@gmail.com"

    // When
    preferencesViewModel.updateUserInfo(user)

    // Then
    val collectJob = launch {
      preferencesViewModel.email.collect { assertEquals(it, expectedEmail) }
    }

    delay(1000) // Wait for 1 second
    collectJob.cancel()
  }

  @Test
  fun `updateUserProfilePictureUrl should update the profilePictureUrl preference`() = runTest {
    // Given
    val user =
        UserModel(
            "123",
            "John Doe",
            "testemail@gmail.com",
            "https://www.example.com/profile.jpg",
            "I'm a cool guy",
            HikingLevel.BEGINNER)

    val expectedProfilePictureUrl = "https://www.example.com/profile.jpg"

    // When
    preferencesViewModel.updateUserInfo(user)

    // Then
    val collectJob = launch {
      preferencesViewModel.profilePictureUrl.collect { assertEquals(it, expectedProfilePictureUrl) }
    }

    delay(1000) // Wait for 1 second
    collectJob.cancel()
  }

  @Test
  fun `updateUserHikingLevel should update the hikingLevel preference`() = runTest {
    // Given
    val user =
        UserModel(
            "123",
            "John Doe",
            "testemail@gmail.com",
            "https://www.example.com/profile.jpg",
            "I'm a cool guy",
            HikingLevel.BEGINNER)

    val expectedHikingLevel = HikingLevel.BEGINNER

    // When
    preferencesViewModel.updateUserInfo(user)

    // Then
    val collectJob = launch {
      preferencesViewModel.hikingLevel.collect { assertEquals(it, expectedHikingLevel) }
    }

    delay(1000) // Wait for 1 second
    collectJob.cancel()
  }

  @Test
  fun `updateHikingLevel should update the hikingLevel preference`() = runTest {
    // Given
    val expectedHikingLevel = HikingLevel.INTERMEDIATE

    // When
    preferencesViewModel.updateHikingLevel(expectedHikingLevel)

    // Then
    val collectJob = launch {
      preferencesViewModel.hikingLevel.collect { assertTrue(it == expectedHikingLevel) }
    }

    delay(1000) // Wait for 1 second
    collectJob.cancel()
  }

  @Test
  fun `clearPreferences should clear all preferences`() = runTest {
    // Given
    val user =
        UserModel(
            "123",
            "John Doe",
            "testemail@gmail.com",
            "https://www.example.com/profile.jpg",
            "I'm a cool guy",
            HikingLevel.ADVANCED)
    fakePreferencesRepo.updateUserInfo(user)

    // When
    preferencesViewModel.clearPreferences()

    // Then
    val collectJob2 = launch {
      preferencesViewModel.isLoggedIn.collect { assertFalse(it) }
      preferencesViewModel.userId.collect { assertEquals(it, "") }
      preferencesViewModel.userName.collect { assertEquals(it, "") }
      preferencesViewModel.email.collect { assertEquals(it, "") }
      preferencesViewModel.profilePictureUrl.collect { assertEquals(it, "") }
      preferencesViewModel.hikingLevel.collect { assertEquals(it, HikingLevel.ADVANCED) }
    }

    delay(1000) // Wait for 1 second
    collectJob2.cancel()
  }

  private class FakePreferencesRepository : PreferencesRepository {
    private val userPreferencesStateFlow =
        MutableStateFlow(UserPreferences(false, "", "", "", "", "", HikingLevel.BEGINNER))
    override val userPreferencesFlow: Flow<UserPreferences> = userPreferencesStateFlow

    fun setUserPreferences(userPreferences: UserPreferences) {
      userPreferencesStateFlow.value = userPreferences
    }

    override suspend fun updateIsLoggedIn(isLoggedIn: Boolean) {
      userPreferencesStateFlow.value = userPreferencesStateFlow.value.copy(isLoggedIn = isLoggedIn)
    }

    override suspend fun updateUserInfo(user: UserModel?) {
      this.setUserPreferences(
          userPreferencesStateFlow.value.copy(
              uid = user?.userId ?: "",
              userName = user?.userName ?: "",
              email = user?.email ?: "",
              profilePictureUrl = user?.profilePictureUrl ?: ""))
    }

    override suspend fun updateHikingLevel(hikingLevel: HikingLevel) {
      userPreferencesStateFlow.value =
          userPreferencesStateFlow.value.copy(hikingLevel = hikingLevel)
    }

    override suspend fun clearPreferences() {
      userPreferencesStateFlow.value =
          UserPreferences(false, "", "", "", "", "", HikingLevel.BEGINNER)
    }

    override suspend fun updateBio(bio: String) {
      TODO("Not yet implemented")
    }
  }
}
