package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
  fun `currentUser returns UserModel when auth currentUser is not null`() {
    val mockFirebaseUser = mockk<FirebaseUser>()
    every { mockFirebaseUser.uid } returns "testUserId"
    every { mockFirebaseUser.displayName } returns "Test User"
    every { mockFirebaseUser.email } returns "test@example.com"
    every { mockFirebaseUser.photoUrl.toString() } returns "https://example.com/photo.jpg"

    every { auth.currentUser } returns mockFirebaseUser

    val userModel = authRepository.currentUser

    assertEquals("testUserId", userModel?.userId)
    assertEquals("Test User", userModel?.userName)
    assertEquals("test@example.com", userModel?.email)
    assertEquals("https://example.com/photo.jpg", userModel?.profilePictureUrl.toString())
  }

  @Test
  fun `currentUser returns null when auth currentUser is null`() {
    every { auth.currentUser } returns null

    val userModel = authRepository.currentUser

    assertNull(userModel)
  }

  /*@Test
  fun `startGoogleSignIn returns Success when signIn is successful`() = runTest {
      val mockTask: Task<BeginSignInResult> = mockk()
      val mockResult = mockk<BeginSignInResult>()
      every { oneTapClient.beginSignIn(signInRequest) } returns mockTask
      coEvery { mockTask.await() } returns mockResult

      val result = authRepository.startGoogleSignIn()

      assertTrue(result is Success)
      assertEquals(mockResult, (result as Success).data)
  }*/

  /*@Test
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
  fun `signOut returns Success true`() = runTest {
      coEvery { oneTapClient.signOut() } just runs
      coEvery { auth.signOut() } just runs

      val result = authRepository.signOut()

      assert(result is Success)
      assert((result as Success).data == true)
  }

  @Test
  fun `signOut returns Failure when exception occurs`() = runTest {
      coEvery { oneTapClient.signOut() } throws Exception("OneTap sign out error")

      val result = authRepository.signOut()

      assert(result is Failure)
      assert((result as Failure).e.message == "OneTap sign out error")
  }*/
}
