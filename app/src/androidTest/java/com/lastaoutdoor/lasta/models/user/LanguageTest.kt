package com.lastaoutdoor.lasta.models.user

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.lastaoutdoor.lasta.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class LanguageTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun testGetIconEnglish() {
    composeTestRule.setContent {
      androidx.compose.foundation.Image(
          painter = Language.ENGLISH.getIcon(), contentDescription = "English Icon")
    }

    composeTestRule.onNodeWithContentDescription("English Icon").assertIsDisplayed()
  }

  @Test
  fun testGetIconFrench() {
    composeTestRule.setContent {
      androidx.compose.foundation.Image(
          painter = Language.FRENCH.getIcon(), contentDescription = "French Icon")
    }
    composeTestRule.onNodeWithContentDescription("French Icon").assertIsDisplayed()
  }

  @Test
  fun testGetIconGerman() {
    composeTestRule.setContent {
      androidx.compose.foundation.Image(
          painter = Language.GERMAN.getIcon(), contentDescription = "German Icon")
    }
    composeTestRule.onNodeWithContentDescription("German Icon").assertIsDisplayed()
  }
}
