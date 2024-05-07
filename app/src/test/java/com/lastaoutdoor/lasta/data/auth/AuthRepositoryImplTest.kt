package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.lastaoutdoor.lasta.data.MockTask
import com.lastaoutdoor.lasta.repository.auth.AuthRepository
import com.lastaoutdoor.lasta.repository.db.UserDBRepository
import com.lastaoutdoor.lasta.utils.Response
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

  private lateinit var authRepo : AuthRepository
  private val auth = mockk<FirebaseAuth>(relaxed = true)
  private val oneTapClient = mockk<SignInClient>(relaxed = true)
  private val signInRequest = mockk<BeginSignInRequest>(relaxed = true)
  private val signUpRequest = mockk<BeginSignInRequest>(relaxed = true)
  private val userDBRepo = mockk<UserDBRepository>(relaxed = true)
  private val task: MockTask<BeginSignInResult> = mockk()

  @Before
  fun setup() {
    authRepo = AuthRepositoryImpl(auth, oneTapClient, signInRequest, signUpRequest, userDBRepo)
  }

  @After
  fun tearDown() {
    clearAllMocks()
  }

  @Test
  fun `Is sign Up is false by default`() = runTest {
    authRepo.isSignUp.first().let {
      assert(!it)
    }
  }

  @Test
  fun `Start Google Sign In emits Loading and Success`() = runTest {
    val result = mockk<BeginSignInResult>(relaxed = true)
    var counter = 0
    every { oneTapClient.beginSignIn(signInRequest) } returns task
    every { task.isComplete } returns true
    every { task.isCanceled } returns false
    every { task.isSuccessful } returns true
    every { task.result } returns result
    every { task.exception } returns null
    coEvery { task.await() } returns result
    authRepo.startGoogleSignIn().collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Success)
        val success = it as Response.Success
        assert(success.data == result)
      }
    }
  }

  @Test
  fun `Start Google Sign In emits Loading then fails and goes to sign up then success`() = runTest {
    val result = mockk<BeginSignInResult>(relaxed = true)
    var counter = 0
    every { oneTapClient.beginSignIn(signInRequest) } throws Exception()
    every { oneTapClient.beginSignIn(signUpRequest) } returns task
    every { task.isComplete } returns true
    every { task.isCanceled } returns false
    every { task.isSuccessful } returns true
    every { task.result } returns result
    every { task.exception } returns null
    coEvery { task.await() } returns result
    authRepo.startGoogleSignIn().collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Success)
        val success = it as Response.Success
        assert(success.data == result)
      }
    }
  }

  @Test
  fun `Start Google Sign in emits Loading then fails twice on sign in and sign up`() = runTest {
    var counter = 0
    every { oneTapClient.beginSignIn(signInRequest) } throws Exception()
    every { oneTapClient.beginSignIn(signUpRequest) } throws Exception()
    authRepo.startGoogleSignIn().collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Failure)
      }
    }
  }
}
