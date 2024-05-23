package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.lastaoutdoor.lasta.repository.app.ConnectivityRepository
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.ErrorToast
import com.lastaoutdoor.lasta.viewmodel.repo.FakeAuthRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakeConnectivityviewRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {
  @ExperimentalCoroutinesApi val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
  val flow = flowOf(true)
  val db = FakeAuthRepo(flow)
  val userDB = FakeUserDB()
  val oneTap: SignInClient = mockk()
  private lateinit var vm: AuthViewModel
  val errorToast = mockk<ErrorToast>()
  val connectDb : ConnectivityRepository = FakeConnectivityviewRepo()

  @ExperimentalCoroutinesApi
  @Before
  fun setupDispatcher() {

    Dispatchers.setMain(testDispatcher)
    vm = AuthViewModel(db, userDB, oneTap, errorToast, connectDb)
    //every { connectDb.connectionState } returns flowOf(ConnectionState.CONNECTED)
    every { errorToast.showToast(any()) } returns Unit
  }

  @ExperimentalCoroutinesApi
  @After
  fun tearDownDispatcher() {
    Dispatchers.resetMain()
    vm.viewModelScope.cancel()
    testDispatcher.cleanupTestCoroutines()
  }

  @Test
  fun auth() {
    vm.updateFieldInUser("", ",", "")
    vm.user
    vm.beginSignInResult
    vm.isSignUp
    vm.signedOut
    vm.oneTapClient
    vm.finishGoogleSignIn(mockk())
    vm.signOut()
    vm.startGoogleSignIn()
  }

  @Test
  fun `startGoogleSignIn with exception`() {
    db.shouldThrowException = true
    try {
      vm.startGoogleSignIn()
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
    db.shouldThrowException = false
  }

  @Test
  fun `finishGoogleSignIn with exception`() {
    db.shouldThrowException = true
    try {
      vm.finishGoogleSignIn(mockk())
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
    db.shouldThrowException = false
  }

  @Test
  fun `signOut with exception`() {
    db.shouldThrowException = true
    try {
      vm.signOut()
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
    db.shouldThrowException = false
  }

  @Test
  fun `updateFieldInUser with exception`() {
    db.shouldThrowException = true
    try {
      vm.updateFieldInUser("", ",", "")
    } catch (e: Exception) {
      coVerify { errorToast.showToast(any()) }
    }
    db.shouldThrowException = false
  }
}
