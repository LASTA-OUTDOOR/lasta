package com.lastaoutdoor.lasta.ui.screen.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MapScreenTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  private var isSheetOpen by mutableStateOf(false)
}

  // Test that the bottom sheet works as wanted
  /*
  @OptIn(ExperimentalMaterial3Api::class)
  @Test
  fun bottomSheetTestInitial() {

    composeRule.activity.setContent {
      val sheetState = rememberModalBottomSheetState()
      InformationSheet(
          sheetState = sheetState,
          isSheetOpen = isSheetOpen,
          onDismissRequest = { isSheetOpen = false })
    }

    composeRule.onNodeWithTag("bottomSheet").assertIsNotDisplayed()
    isSheetOpen = true
    composeRule.onNodeWithTag("bottomSheet").assertIsDisplayed()
    isSheetOpen = false
  }
  */

/*
  @OptIn(ExperimentalMaterial3Api::class)
  @Test
  fun bottomSheetTestDismiss() {

    isSheetOpen = true

    composeRule.activity.setContent {
      val viewModel: MapViewModel = hiltViewModel()

      viewModel.state.selectedMarker =
          ClimbingMarker("Test marker", LatLng(0.0, 0.0), "Test description", 1)

      Column {
        Text("Other content")

        val sheetState = rememberModalBottomSheetState()

        InformationSheet(
            sheetState = sheetState,
            isSheetOpen = isSheetOpen,
            onDismissRequest = { isSheetOpen = false })
      }
    }
    composeRule.onNodeWithTag("bottomSheet").assertIsDisplayed()
    composeRule.onNodeWithText("Test marker").assertIsDisplayed()
    Espresso.pressBack()
    composeRule.onNodeWithTag("bottomSheet").assertIsNotDisplayed()
  }
}
*/
