package com.lastaoutdoor.lasta.ui.screen.map

import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.ui.screen.discovery.DiscoveryScreen
import com.lastaoutdoor.lasta.ui.screen.profile.ProfileScreen
import com.lastaoutdoor.lasta.ui.theme.LastaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MapScreenTest {

    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = androidx.navigation.compose.rememberNavController()
            com.lastaoutdoor.lasta.ui.theme.LastaTheme {
                androidx.navigation.compose.NavHost(
                    navController = navController, startDestination = "DiscoveryScreen") {
                    composable(route = "DiscoveryScreen") {
                        com.lastaoutdoor.lasta.ui.screen.discovery.DiscoveryScreen()
                    }
                }
            }
        }
    }

    @Test
    fun discoveryScreen_isDisplayed() {
        composeRule.onNodeWithTag("Discovery").assertIsDisplayed()
    }


}
