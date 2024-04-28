package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.repository.auth.AuthRepository

// @RunWith(RobolectricTestRunner::class)
// @Config(manifest = Config.NONE)
class AuthRepositoryImplTest {

  private lateinit var auth: FirebaseAuth
  private lateinit var oneTapClient: SignInClient
  private lateinit var signInRequest: BeginSignInRequest
  private lateinit var repository: AuthRepository

  /*@Before
  fun setup() {
    auth = mockk(relaxed = true)
    oneTapClient = mockk(relaxed = true)
    signInRequest = mockk(relaxed = true)

    repository = AuthRepositoryImpl(auth, oneTapClient, signInRequest)
  }

  @Test
  fun `startGoogleSignIn should emit Success`() = runTest {
    // Given
    val signInResult = mockk<BeginSignInResult>()
    coEvery { oneTapClient.beginSignIn(signInRequest).await() } returns signInResult

    println("Hello")
    // When
    val flow = repository.startGoogleSignIn()

    // Then
    flow.collect { response ->
      when (response) {
        is Response.Loading -> {
          // Do nothing or verify loading state if required
        }
        is Response.Success -> {
          assertEquals(signInResult, response.data)
        }
        is Response.Failure -> {
          fail("Should not be a failure state")
        }
      }
    }

    coVerify { oneTapClient.beginSignIn(signInRequest).await() }
  }

  @Test
  fun `startGoogleSignIn should emit Failure`() = runTest {
    // Given
    val exception = Exception("Test Exception")
    coEvery { oneTapClient.beginSignIn(signInRequest).await() } throws exception

    // When
    val flow = repository.startGoogleSignIn()

    // Then
    flow.collect { response ->
      when (response) {
        is Response.Loading -> {
          // Do nothing or verify loading state if required
        }
        is Response.Success -> {
          fail("Should not be a success state")
        }
        is Response.Failure -> {
          assertEquals(exception, response.e)
        }
      }
    }

    verify { oneTapClient.beginSignIn(signInRequest).await() }
  }

  @Test
  fun `finishGoogleSignIn should emit Success`() = runTest {
    // Given
    val googleCredential = mockk<AuthCredential>()
    val signInTask = mockk<SignInTask>()
    val user = mockk<FirebaseUser>()
    val userModel = UserModel("uid", "displayName", "email", "photoUrl", null)

    coEvery { auth.signInWithCredential(googleCredential).await() } returns signInTask
    coEvery { signInTask.user } returns user
    every { user.uid } returns "uid"
    every { user.displayName } returns "displayName"
    every { user.email } returns "email"
    every { user.photoUrl } returns mockk { every { toString() } returns "photoUrl" }

    // When
    val flow = repository.finishGoogleSignIn(googleCredential)

    // Then
    flow.collect { response ->
      when (response) {
        is Response.Loading -> {
          // Do nothing or verify loading state if required
        }
        is Response.Success -> {
          assertEquals(userModel, response.data)
        }
        is Response.Failure -> {
          fail("Should not be a failure state")
        }
      }
    }

    verify { auth.signInWithCredential(googleCredential).await() }
  }

  @Test
  fun `finishGoogleSignIn should emit Failure`() = runBlockingTest {
    // Given
    val googleCredential = mockk<AuthCredential>()
    val exception = Exception("Test Exception")

    coEvery { auth.signInWithCredential(googleCredential).await() } throws exception

    // When
    val flow = repository.finishGoogleSignIn(googleCredential)

    // Then
    flow.collect { response ->
      when (response) {
        is Response.Loading -> {
          // Do nothing or verify loading state if required
        }
        is Response.Success -> {
          fail("Should not be a success state")
        }
        is Response.Failure -> {
          assertEquals(exception, response.exception)
        }
      }
    }

    verify { auth.signInWithCredential(googleCredential).await() }
  }

  @Test
  fun `signOut should emit Success`() = runBlockingTest {
    // Given
    val signOutTask = mockk<SignOutTask>()

    coEvery { oneTapClient.signOut().await() } returns signOutTask
    coEvery { auth.signOut() } returns Unit

    // When
    val flow = repository.signOut()

    // Then
    flow.collect { response ->
      when (response) {
        is Response.Loading -> {
          // Do nothing or verify loading state if required
        }
        is Response.Success -> {
          assertTrue(response.data!!)
        }
        is Response.Failure -> {
          fail("Should not be a failure state")
        }
      }
    }

    verify {
      oneTapClient.signOut().await()
      auth.signOut()
    }
  }

  @Test
  fun `signOut should emit Failure`() = runBlockingTest {
    // Given
    val exception = Exception("Test Exception")

    coEvery { oneTapClient.signOut().await() } throws exception

    // When
    val flow = repository.signOut()

    // Then
    flow.collect { response ->
      when (response) {
        is Response.Loading -> {
          // Do nothing or verify loading state if required
        }
        is Response.Success -> {
          fail("Should not be a success state")
        }
        is Response.Failure -> {
          assertEquals(exception, response.exception)
        }
      }
    }

    verify { oneTapClient.signOut().await() }
  }*/
}
