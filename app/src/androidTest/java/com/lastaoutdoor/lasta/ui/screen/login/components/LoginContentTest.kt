package com.lastaoutdoor.lasta.ui.screen.login.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.data.api.FakeOutdoorActivityRepository
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.screen.discovery.DiscoveryScreen
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
class LoginContentTest {
    // Allow Hilt to inject dependencies
    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

    // Create a compose rule
    @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()
    // Set up the test
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent { LoginContent{} }
    }

    // Test if discovery screen is displayed
    @Test
    fun loginContent_isDisplayed() {
        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
    }

    @Test
    fun logo_isDisplayed() {
        composeRule.onNodeWithTag("loginLogo").assertIsDisplayed()
    }

    @Test
    fun appname_isDisplayed() {
        composeRule.onNodeWithTag("appName").assertIsDisplayed()
    }

    @Test
    fun button_isDisplayed() {
        composeRule.onNodeWithTag("loginButton").assertIsDisplayed()
    }
}