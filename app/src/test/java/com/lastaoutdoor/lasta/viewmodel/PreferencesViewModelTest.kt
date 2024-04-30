package com.lastaoutdoor.lasta.viewmodel

class PreferencesViewModelTest {

  /*private lateinit var fakePreferencesRepo: FakePreferencesRepository
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

    // val collectJob = launch { preferencesViewModel.isLoggedIn.collect { assertTrue(it) } }

    // delay(1000) // Wait for 1 second
    // collectJob.cancel()
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
            UserLevel.BEGINNER)

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
            UserLevel.INTERMEDIATE)

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
            UserLevel.ADVANCED)

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
            UserLevel.BEGINNER)

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
            UserLevel.BEGINNER)

    val expectedUserLevel = UserLevel.BEGINNER

    // When
    preferencesViewModel.updateUserInfo(user)

    // Then
    val collectJob = launch {
      preferencesViewModel.hikingLevel.collect { assertEquals(it, expectedUserLevel) }
    }

    delay(1000) // Wait for 1 second
    collectJob.cancel()
  }

  @Test
  fun `updateHikingLevel should update the hikingLevel preference`() = runTest {
    // Given
    val expectedUserLevel = UserLevel.INTERMEDIATE

    // When
    preferencesViewModel.updateHikingLevel(expectedUserLevel)

    // Then
    val collectJob = launch {
      preferencesViewModel.hikingLevel.collect { assertTrue(it == expectedUserLevel) }
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
            UserLevel.ADVANCED)
    fakePreferencesRepo.updateUserInfo(user)

    // When
    preferencesViewModel.clearPreferences()

    // Then
    val collectJob2 = launch {
      // preferencesViewModel.isLoggedIn.collect { assertFalse(it) }
      preferencesViewModel.userId.collect { assertEquals(it, "") }
      preferencesViewModel.userName.collect { assertEquals(it, "") }
      preferencesViewModel.email.collect { assertEquals(it, "") }
      preferencesViewModel.profilePictureUrl.collect { assertEquals(it, "") }
      preferencesViewModel.hikingLevel.collect { assertEquals(it, UserLevel.ADVANCED) }
    }

    delay(1000) // Wait for 1 second
    collectJob2.cancel()
  }

  private class FakePreferencesRepository : PreferencesRepository {
    private val userPreferencesStateFlow =
        MutableStateFlow(
            UserPreferences(false, "", "", "", "", "", UserLevel.BEGINNER, "English", "Hiking"))
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

    override suspend fun updateHikingLevel(userLevel: UserLevel) {
      userPreferencesStateFlow.value = userPreferencesStateFlow.value.copy(userLevel = userLevel)
    }

    override suspend fun updateBio(bio: String) {
      TODO("Not yet implemented")
    }

    override suspend fun updateLanguage(language: String) {
      userPreferencesStateFlow.value = userPreferencesStateFlow.value.copy(language = language)
    }

    override suspend fun updatePrefSport(prefSport: String) {
      userPreferencesStateFlow.value = userPreferencesStateFlow.value.copy(prefSport = prefSport)
    }

    override suspend fun clearPreferences() {
      userPreferencesStateFlow.value =
          UserPreferences(false, "", "", "", "", "", UserLevel.BEGINNER, "English", "Hiking")
    }
  }*/
}
