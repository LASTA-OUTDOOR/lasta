package com.lastaoutdoor.lasta.ui.screen.main

import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.navigation.RootScreen
import com.lastaoutdoor.lasta.ui.screen.loading.LoadingScreen
import com.lastaoutdoor.lasta.ui.screen.login.LoginScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MainScreenTest {

    // Allow Hilt to inject dependencies
    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

    // Create a compose rule
    @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

    // Set up the test
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                modifier = Modifier.testTag("MainAppNavGraph"),
                route = RootScreen.Root.route,
                startDestination = RootScreen.Main.route) {
                composable(RootScreen.Loading.route) { LoadingScreen(navController = navController) }
                composable(RootScreen.Login.route) { LoginScreen(navController = navController) }
                composable(RootScreen.Main.route) { MainScreen(rootNavController = navController) }
            }
        }
    }

    // Test the main navigation for login and initial loading
    @Test
    fun mainAppNavGraphIsDisplayed() {
        // Check if the main nav graph is present
        composeRule.onNodeWithTag("MainScreen").assertIsDisplayed()
    }
}
