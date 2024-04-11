package com.lastaoutdoor.lasta.ui.screen.discovery

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.activity.OutdoorActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiscoveryScreenTest {
    /**
     * This test is used to check if the DiscoveryScreen is displayed correctly, if the list of outdoor activities is displayed correctly, and if the dialog is displayed correctly.
     */

    /**
     *    val discoveryScreen : KNode = onNode{hasTestTag("discoveryScreen") }
     *     val discoveryContent : KNode = onNode{hasTestTag("discoveryContent") }
     *     val floatingActionButtons : KNode = onNode{hasTestTag("floatingActionButtons") }
     *     val outdoorActivityList : KNode = onNode{hasTestTag("outdoorActivityList") }
     *     val outdoorActivityItem : KNode = onNode{hasTestTag("outdoorActivityItem") }
     *     val activityDialog : KNode = onNode{hasTestTag("activityDialog") }
     */

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun discoveryScreenTest() {
        composeTestRule.setContent {
            val discoveryScreen = DiscoveryScreen()
        }

        // Check if the DiscoveryScreen is displayed correctly
        composeTestRule.onNodeWithTag("discoveryScreen").assertIsDisplayed()

        // Check if the list of outdoor activities is displayed correctly
        composeTestRule.onNodeWithTag("outdoorActivityList").assertIsDisplayed()

        // Check if the dialog is displayed correctly
        composeTestRule.onNodeWithTag("activityDialog").assertIsDisplayed()

        //Checdk if the floating action buttons are displayed correctly
        composeTestRule.onNodeWithTag("floatingActionButtons").assertIsDisplayed()

        // Check if the outdoor activity item is displayed correctly
        composeTestRule.onNodeWithTag("outdoorActivityItem").assertIsDisplayed()

    }

    @Test
    fun discoveryContentTest() {
        composeTestRule.setContent {
            val discoveryContent = DiscoveryContent()
        }

        // Check if the DiscoveryContent is displayed correctly
        composeTestRule.onNodeWithTag("discoveryContent").assertIsDisplayed()
    }

    @Test
    fun floatingActionButtonsTest() {
        composeTestRule.setContent {
            val floatingActionButtons = FloatingActionButtons()
        }

        // Check if the FloatingActionButtons is displayed correctly
        composeTestRule.onNodeWithTag("floatingActionButtons").assertIsDisplayed()
    }

    @Test
    fun outdoorActivityListTest() {
        composeTestRule.setContent {
            val outdoorActivityList = OutdoorActivityList(listOf(OutdoorActivity(ActivityType.BIKING, 9, 22.3f, "", "")))
        }

        // Check if the OutdoorActivityList is displayed correctly
        composeTestRule.onNodeWithTag("outdoorActivityList").assertIsDisplayed()
    }

    @Test
    fun outdoorActivityItemTest() {
        composeTestRule.setContent {
            val outdoorActivityItem = OutdoorActivityItem(OutdoorActivity(ActivityType.BIKING, 9, 22.3f, "", ""))
        }

        // Check if the OutdoorActivityItem is displayed correctly
        composeTestRule.onNodeWithTag("outdoorActivityItem").assertIsDisplayed()
    }


}