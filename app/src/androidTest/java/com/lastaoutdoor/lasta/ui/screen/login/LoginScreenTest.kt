package com.lastaoutdoor.lasta.ui.screen.login

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.SignInClient
import com.lastaoutdoor.lasta.data.api.FakeOutdoorActivityRepository
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.repository.AuthRepository
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.DiscoveryScreenViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class LoginScreenTest {
    // Allow Hilt to inject dependencies
    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

    // Create a compose rule
    @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

    // Set up the test
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent { LoginScreen(navController = rememberNavController())}
    }

    @Test
    fun loginScreen_isdisplayed(){
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
    }
}