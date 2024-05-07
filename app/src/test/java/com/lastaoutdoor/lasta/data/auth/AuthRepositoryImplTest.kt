package com.lastaoutdoor.lasta.data.auth

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lastaoutdoor.lasta.data.MockTask
import com.lastaoutdoor.lasta.models.user.UserModel
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
  private val startLoginTask: MockTask<BeginSignInResult> = mockk()
  private val signInWithCredentialTask: MockTask<AuthResult> = mockk()
  private val signOutTask: MockTask<Void> = mockk()

  private val beginSignInResult: BeginSignInResult = mockk(relaxed = true)
  private val authResult: AuthResult = mockk(relaxed = true)

  @Before
  fun setup() {
    authRepo = AuthRepositoryImpl(auth, oneTapClient, signInRequest, signUpRequest, userDBRepo)
    every { startLoginTask.isComplete } returns true
    every { startLoginTask.isCanceled } returns false
    every { startLoginTask.isSuccessful } returns true
    every { startLoginTask.result } returns beginSignInResult
    every { startLoginTask.exception } returns null
    coEvery { startLoginTask.await() } returns beginSignInResult

    every { signInWithCredentialTask.isComplete } returns true
    every { signInWithCredentialTask.isCanceled } returns false
    every { signInWithCredentialTask.isSuccessful } returns true
    every { signInWithCredentialTask.result } returns authResult
    every { signInWithCredentialTask.exception } returns null
    coEvery { signInWithCredentialTask.await() } returns authResult

    every { signOutTask.isComplete } returns true
    every { signOutTask.isCanceled } returns false
    every { signOutTask.isSuccessful } returns true
    every { signOutTask.result } returns mockk()
    every { signOutTask.exception } returns null
    coEvery { signOutTask.await() } returns mockk()
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
    var counter = 0
    every { oneTapClient.beginSignIn(signInRequest) } returns startLoginTask
    authRepo.startGoogleSignIn().collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Success)
        val success = it as Response.Success
        assert(success.data == beginSignInResult)
      }
    }
  }

  @Test
  fun `Start Google Sign In emits Loading then fails and goes to sign up then success`() = runTest {
    var counter = 0
    every { oneTapClient.beginSignIn(signInRequest) } throws Exception()
    every { oneTapClient.beginSignIn(signUpRequest) } returns startLoginTask
    authRepo.startGoogleSignIn().collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Success)
        val success = it as Response.Success
        assert(success.data == beginSignInResult)
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

  @Test
  fun `Finish Google sign in emits Loading then with null user fails`() = runTest {
    var counter = 0
    every { auth.signInWithCredential(any()) } returns signInWithCredentialTask
    every { authResult.user } returns null
    authRepo.finishGoogleSignIn(mockk()).collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Failure)
        val failure = it as Response.Failure
        assert(failure.e.message == "User is null")
      }
    }
  }

  @Test
  fun `Finish Google sign in emits loading then when throwing returns a failure`() = runTest {
    var counter = 0
    every { auth.signInWithCredential(any()) } throws Exception()
    authRepo.finishGoogleSignIn(mockk()).collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Failure)
      }
    }
  }

  @Test
  fun `Finish Google Sign in works for sign up`() = runTest {
    var counter = 0
    every { auth.signInWithCredential(any()) } returns signInWithCredentialTask
    val user: FirebaseUser = mockk(relaxed = true)
    every { authResult.user } returns user
    every { user.uid } returns "123"
    every { user.displayName } returns "John"
    every { user.email } returns "test@email.com"
    every { user.photoUrl } returns null
    every { authResult.additionalUserInfo?.isNewUser } returns true
    every { userDBRepo.updateUser(any()) } returns Unit
    authRepo.finishGoogleSignIn(mockk()).collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Success)
        val success = it as Response.Success
        assert(success.data is UserModel)
      }
    }
  }

  @Test
  fun `Finish Google Sign in works for sign in`() = runTest {
    var counter = 0
    every { auth.signInWithCredential(any()) } returns signInWithCredentialTask
    val user: FirebaseUser = mockk(relaxed = true)
    val dbUser = UserModel("test")
    every { authResult.user } returns user
    every { user.uid } returns "123"
    every { user.displayName } returns "John"
    every { user.email } returns "test@email.com"
    every { user.photoUrl } returns null
    every { authResult.additionalUserInfo?.isNewUser } returns false
    coEvery { userDBRepo.getUserById(any()) } returns dbUser
    authRepo.finishGoogleSignIn(mockk()).collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Success)
        val success = it as Response.Success
        assert(success.data is UserModel)
      }
    }
  }

  @Test
  fun `Finish Google Sign in works for sign in but doesn't find user in DB`() = runTest {
    var counter = 0
    every { auth.signInWithCredential(any()) } returns signInWithCredentialTask
    val user: FirebaseUser = mockk(relaxed = true)
    every { authResult.user } returns user
    every { user.uid } returns "123"
    every { user.displayName } returns "John"
    every { user.email } returns "test@email.com"
    every { user.photoUrl } returns null
    every { authResult.additionalUserInfo } returns null
    coEvery { userDBRepo.getUserById(any()) } returns null
    authRepo.finishGoogleSignIn(mockk()).collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Failure)
        val failure = it as Response.Failure
        assert(failure.e.message == "User data not found")
      }
    }
  }

  @Test
  fun `Sign out throws exception so failure after loading`() = runTest {
    var counter = 0
    every { oneTapClient.signOut() } throws Exception()
    authRepo.signOut().collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Failure)
      }
    }
  }

  @Test
  fun `Sign out works emits loading then success`() = runTest {
    var counter = 0
    every { oneTapClient.signOut() } returns signOutTask
    every { auth.signOut() } returns Unit
    authRepo.signOut().collect {
      if (counter == 0) {
        assert(it is Response.Loading)
        counter++
      } else {
        assert(it is Response.Success)
        val result = it as Response.Success
        assert(result.data == true)
      }
    }
  }
}
