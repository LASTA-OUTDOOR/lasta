package com.lastaoutdoor.lasta.viewmodel

import com.google.android.gms.auth.api.identity.SignInClient
import com.lastaoutdoor.lasta.viewmodel.repo.FakeAuthRepo
import com.lastaoutdoor.lasta.viewmodel.repo.FakeUserDB
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {
    @ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    val flow = flowOf(true)
    val db = FakeAuthRepo(flow)
    val userDB = FakeUserDB()
    val oneTap : SignInClient = mockk()
    private lateinit var vm : AuthViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setupDispatcher() {

        Dispatchers.setMain(testDispatcher)
        vm = AuthViewModel(db, userDB, oneTap)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
    @Test
    fun auth(){
        vm.updateFieldInUser("",",","")
        vm.user
        vm.beginSignInResult
        vm.isSignUp
        vm.signedOut
        vm.oneTapClient
        vm.finishGoogleSignIn(mockk())
        vm.signOut()
        vm.startGoogleSignIn()

    }

}
