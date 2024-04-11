package com.lastaoutdoor.lasta.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.auth.api.identity.SignInClient
import com.lastaoutdoor.lasta.repository.AuthRepository
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(manifest = Config.NONE)
class AuthViewModelTest {

  @get:Rule val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

  private lateinit var authRepo: AuthRepository
  private lateinit var signInClient: SignInClient
  private lateinit var viewModel: AuthViewModel

  @Before
  fun setup() {
    authRepo = mockk()
    signInClient = mockk()
    viewModel = AuthViewModel(authRepo, signInClient)
  }

  /*@Test
  fun `startGoogleSignIn sets loading and updates oneTapSignInResponse`() = runTest {
    // Given
    val mockResponse: Response<BeginSignInResult> = mockk()

    coEvery { authRepo.startGoogleSignIn() } returns mockResponse

    // When
    viewModel.startGoogleSignIn()

    // Then
    assert(viewModel.oneTapSignInResponse == mockResponse)
  }

  @Test
  fun `finishGoogleSignIn sets loading and updates signInWithGoogleResponse`() = runTest {
    // Given
    val googleCredential: AuthCredential = mockk()
    coEvery { authRepo.finishGoogleSignIn(googleCredential) } returns Success(mockk())

    // When
    viewModel.finishGoogleSignIn(googleCredential)

    // Then
    assert(viewModel.signInWithGoogleResponse is Success)
  }

  @Test
  fun `signOut sets loading and updates signOutResponse`() = runTest {
    // Given
    coEvery { authRepo.signOut() } returns Success(true)

    // When
    viewModel.signOut()

    // Then
    assert(viewModel.signOutResponse is Success)
  }*/
}
