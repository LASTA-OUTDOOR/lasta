package com.lastaoutdoor.lasta.viewmodel

class AuthViewModelTest {

  /*private lateinit var fakeAuthRepo: FakeAuthRepository
  private lateinit var mockOneTapClient: SignInClient
  private lateinit var authViewModel: AuthViewModel

  @Before
  fun setUp() {
    fakeAuthRepo = FakeAuthRepository()
    mockOneTapClient = mockk<SignInClient>()
    authViewModel = AuthViewModel(fakeAuthRepo, mockOneTapClient)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `oneTapClient should be initialized`() {
    assertEquals(mockOneTapClient, authViewModel.oneTapClient)
  }

  @Test
  fun `startGoogleSignIn should updates the flow accordingly`() = runTest {
    // Given
    authViewModel.startGoogleSignIn()
    assertEquals(null, authViewModel.beginSignInResult)
    val expectedResult = mockk<BeginSignInResult>()
    fakeAuthRepo.setBeginSignInResult(MutableStateFlow(Response.Success(expectedResult)))

    // When
    authViewModel.startGoogleSignIn()

    // Then
    assertEquals(expectedResult, authViewModel.beginSignInResult)
  }

  /*@Test
  fun `startGoogleSignIn should throw exception on failure`() = runTest {
    // Given
    fakeAuthRepo.setShouldThrow(true)

    assertThrows(Exception::class.java) {
      // When
      authViewModel.startGoogleSignIn()
    }
  }*/

  @Test
  fun `finishGoogleSignIn should update user`() = runTest {
    // Given
    authViewModel.finishGoogleSignIn(mockk())
    assertEquals(null, authViewModel.user)
    val expectedResult = mockk<UserModel>()
    fakeAuthRepo.setUser(MutableStateFlow(Response.Success(expectedResult)))

    // When
    authViewModel.finishGoogleSignIn(mockk())

    // Then
    assertEquals(expectedResult, authViewModel.user)
  }

  @Test
  fun `signOut should update signedOut`() = runTest {
    // Given
    authViewModel.signOut()
    assertEquals(false, authViewModel.signedOut)
    fakeAuthRepo.setSignOutResult(MutableStateFlow(Response.Success(true)))

    // When
    authViewModel.signOut()

    // Then
    assertTrue(authViewModel.signedOut)
  }

  // Mock classes for testing
  private class FakeAuthRepository : AuthRepository {
    private var beginSignInResultFlow =
        MutableStateFlow<Response<BeginSignInResult>>(Response.Loading)
    private var userFlow = MutableStateFlow<Response<UserModel>>(Response.Loading)
    private var signOutFlow = MutableStateFlow<Response<Boolean>>(Response.Loading)

    private var shouldThrow: Boolean = false

    private var beginSignInResultExceptionFlow =
        MutableStateFlow<Response<BeginSignInResult>>(Response.Failure(Exception("Test exception")))
    private var userExceptionFlow =
        MutableStateFlow<Response<UserModel>>(Response.Failure(Exception("Test exception")))
    private var signOutExceptionFlow =
        MutableStateFlow<Response<Boolean>>(Response.Failure(Exception("Test exception")))

    fun setBeginSignInResult(flow: MutableStateFlow<Response<BeginSignInResult>>) {
      beginSignInResultFlow = flow
    }

    fun setUser(flow: MutableStateFlow<Response<UserModel>>) {
      userFlow = flow
    }

    fun setSignOutResult(flow: MutableStateFlow<Response<Boolean>>) {
      signOutFlow = flow
    }

    fun setShouldThrow(shouldThrow: Boolean) {
      this.shouldThrow = shouldThrow
    }

    override fun observeIsSignUp(): Flow<Boolean> {
      return MutableStateFlow(false)
    }

    override suspend fun startGoogleSignIn(): Flow<Response<BeginSignInResult>> {
      return if (shouldThrow) {
        beginSignInResultExceptionFlow
      } else {
        beginSignInResultFlow
      }
    }

    override suspend fun finishGoogleSignIn(
        googleCredential: AuthCredential
    ): Flow<Response<UserModel>> {
      return if (shouldThrow) {
        userExceptionFlow
      } else {
        userFlow
      }
    }

    override suspend fun signOut(): Flow<Response<Boolean>> {
      return if (shouldThrow) {
        signOutExceptionFlow
      } else {
        signOutFlow
      }
    }
  }*/
}
