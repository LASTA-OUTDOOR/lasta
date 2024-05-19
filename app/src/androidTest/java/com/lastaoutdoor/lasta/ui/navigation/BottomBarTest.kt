package com.lastaoutdoor.lasta.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Locale

@HiltAndroidTest
@UninstallModules(AppModule::class)
class BottomBarTest {
  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  val tabs: Array<BottomBarTab> =
      arrayOf(
          BottomBarTab.DISCOVER, BottomBarTab.FAVORITES, BottomBarTab.SOCIALS, BottomBarTab.PROFILE)

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun testBottomBar() {
    composeRule.activity.setContent { BottomBar(tabs, "Discover") {} }
    composeRule.onNodeWithTag("bottomBar").assertIsDisplayed()
  }

  // Test with german language
  @Test
  fun testBottomBarWithGermanLanguage() {
    composeRule.activity.setContent {
      Locale.setDefault(Locale("de"))
      BottomBar(tabs, "Discover") {}
    }
    composeRule.onNodeWithTag("bottomBar").assertIsDisplayed()
  }
}
