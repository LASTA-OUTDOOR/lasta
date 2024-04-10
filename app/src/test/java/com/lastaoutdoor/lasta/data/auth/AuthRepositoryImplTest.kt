package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.repository.AuthRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthRepositoryImplTest {

  private lateinit var authRepository: AuthRepository
  private lateinit var auth: FirebaseAuth
  private lateinit var oneTapClient: SignInClient
  private lateinit var signInRequest: BeginSignInRequest

  @Before
  fun setUp() {
    auth = mockk(relaxed = true)
    oneTapClient = mockk()
    signInRequest = mockk()

    authRepository = AuthRepositoryImpl(auth, oneTapClient, signInRequest)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `isUserAuthenticated returns true when currentUser is not null`() {
    every { auth.currentUser } returns mockk(relaxed = true)

    assertTrue(authRepository.isUserAuthentificated)
  }

  /*@Test
  fun `isUserAuthenticated returns false when currentUser is null`() {
      every { auth.currentUser } returns null

      println(authRepository.isUserAuthentificated)

      assertFalse(authRepository.isUserAuthentificated)
  }

  @ExperimentalCoroutinesApi
  @Test
  fun `currentUser returns UserModel when auth currentUser is not null`() {
      val mockFirebaseUser = mockk<FirebaseUser>()
      every { mockFirebaseUser.uid } returns "testUserId"
      every { mockFirebaseUser.displayName } returns "Test User"
      every { mockFirebaseUser.email } returns "test@example.com"
      every { mockFirebaseUser.photoUrl } answers {
          mockk {
              every { toString() } returns "https://example.com/photo.jpg"
          }
      }

      every { auth.currentUser } returns mockFirebaseUser

      println(auth.currentUser?.uid)

      val userModel = authRepository.currentUser

      assert(userModel?.userId == "testUserId")
      assert(userModel?.userName == "Test User")
      assert(userModel?.email == "test@example.com")
      assert(userModel?.profilePictureUrl == "https://example.com/photo.jpg")
  }

  @Test
  fun `startGoogleSignIn returns Success with OneTapSignInResponse`() = runTest {
      val mockSignInResult = mockk<BeginSignInResult>()
      coEvery { oneTapClient.beginSignIn(any()) } returns mockk {
          coEvery { await() } returns mockSignInResult
      }

      val result = authRepository.startGoogleSignIn()

      assert(result is Success)
      assert((result as Success).data == mockSignInResult)
  }

  @Test
  fun `finishGoogleSignIn returns Success with UserModel`() = runBlockingTest {
      val mockAuthCredential = mockk<AuthCredential>()
      val mockFirebaseUser = mockk<FirebaseUser>()
      every { mockFirebaseUser.uid } returns "testUserId"
      every { mockFirebaseUser.displayName } returns "Test User"
      every { mockFirebaseUser.email } returns "test@example.com"
      every { mockFirebaseUser.photoUrl } returns mockk {
          every { toString() } returns "https://example.com/photo.jpg"
      }
      coEvery { auth.signInWithCredential(any()) } returns mockk {
          coEvery { await() } returns mockk {
              every { user } returns mockFirebaseUser
          }
      }

      val result = authRepository.finishGoogleSignIn(mockAuthCredential)

      assert(result is Success)
      val userModel = (result as Success).data
      assert(userModel.userId == "testUserId")
      assert(userModel.userName == "Test User")
      assert(userModel.email == "test@example.com")
      assert(userModel.profilePictureUrl == "https://example.com/photo.jpg")
  }

  @Test
  fun `finishGoogleSignIn returns Failure when user is null`() = runBlockingTest {
      val mockAuthCredential = mockk<AuthCredential>()
      coEvery { auth.signInWithCredential(any()) } returns mockk {
          coEvery { await() } returns mockk {
              every { user } returns null
          }
      }

      val result = authRepository.finishGoogleSignIn(mockAuthCredential)

      assert(result is Failure)
      assert((result as Failure).exception.message == "User is null")
  }

  @Test
  fun `signOut returns Success true`() = runBlockingTest {
      coEvery { oneTapClient.signOut() } just runs
      coEvery { auth.signOut() } just runs

      val result = authRepository.signOut()

      assert(result is Success)
      assert((result as Success).data == true)
  }

  @Test
  fun `signOut returns Failure when exception occurs`() = runBlockingTest {
      coEvery { oneTapClient.signOut() } throws Exception("OneTap sign out error")

      val result = authRepository.signOut()

      assert(result is Failure)
      assert((result as Failure).exception.message == "OneTap sign out error")
  }*/
}
