package com.lastaoutdoor.lasta.ui.screen.discover.components

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class LocalitySelectionDropdownTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun localitySelectionDropdown_isDisplayed() {
    composeRule.activity.setContent {
      LocalitySelectionDropdown(
          localities = listOf(Pair("locality1", LatLng(0.0, 0.0))),
          selectedLocality = Pair("locality1", LatLng(0.0, 0.0)),
          setSelectedLocality = {})
    }
    composeRule.onNodeWithText("locality1").performClick()
    composeRule.onNodeWithTag("localitySelectionDropdownItem").assertIsDisplayed()
    composeRule.onNodeWithTag("localitySelectionDropdownItem").performClick()
    composeRule.onNodeWithTag("localitySelectionDropdownItem").assertIsNotDisplayed()
  }
}
