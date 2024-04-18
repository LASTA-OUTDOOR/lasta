package com.lastaoutdoor.lasta.ui.screen.social

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lastaoutdoor.lasta.data.model.profile.ActivitiesDatabaseType
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.di.AppModule
import com.lastaoutdoor.lasta.repository.SocialRepository
import com.lastaoutdoor.lasta.ui.MainActivity
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FakeSocialRepository : SocialRepository {

  var friends: ArrayList<UserModel> = ArrayList()
  var activities: ArrayList<ActivitiesDatabaseType> = ArrayList()
  var messages: ArrayList<String> = ArrayList()

  fun setFriends(friends: List<UserModel>) {
    this.friends.addAll(friends)
  }

  override fun getFriends(): List<UserModel>? {
    return friends
  }

  override fun getLatestFriendActivities(days: Int): List<ActivitiesDatabaseType>? {
    return activities
  }

  override fun getMessages(): List<String>? {
    return messages
  }

  fun clean() {
    friends.clear()
    activities.clear()
    messages.clear()
  }
}

@HiltAndroidTest
@UninstallModules(AppModule::class)
class SocialScreenKtTest {

  @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Viewmodel and repository
  private lateinit var socialViewModel: SocialViewModel

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun initialState() {

    // Set the content to the social screen
    composeRule.activity.setContent { SocialScreen() }

    // Header (title)
    composeRule.onNodeWithTag("Header").assertIsDisplayed()

    // no button in header by default
    composeRule.onNodeWithTag("HeaderButton").assertIsNotDisplayed()

    // The tree tabs
    composeRule.onNodeWithText("Feed").assertIsDisplayed()
    composeRule.onNodeWithText("Friends").assertIsDisplayed()
    composeRule.onNodeWithText("Message").assertIsDisplayed()

    // First tab shows connection error
    composeRule.onNodeWithTag("ConnectionMissing").assertIsDisplayed()
  }

  @Test
  fun testTabSelection(){
    // Set the content to the social screen
    composeRule.activity.setContent { SocialScreen() }

    // initial tab selection
    composeRule.onNodeWithText("Feed").assertIsSelected()

    // Click on the friends tab
    composeRule.onNodeWithText("Friends").performClick()

    // Check if the friends tab is selected
    composeRule.onNodeWithText("Friends").assertIsSelected()
    composeRule.onNodeWithText("Message").assertIsNotSelected()
    composeRule.onNodeWithText("Feed").assertIsNotSelected()
    composeRule.onNodeWithTag("TopButton").assertIsDisplayed()

    // Click on the message tab
    composeRule.onNodeWithText("Message").performClick()

    // Check if the message tab is selected
    composeRule.onNodeWithText("Friends").assertIsNotSelected()
    composeRule.onNodeWithText("Message").assertIsSelected()
    composeRule.onNodeWithText("Feed").assertIsNotSelected()
    composeRule.onNodeWithTag("TopButton").assertIsDisplayed()


    // Click on the feed tab
    composeRule.onNodeWithText("Feed").performClick()

    // Check if the feed tab is selected
    composeRule.onNodeWithText("Friends").assertIsNotSelected()
    composeRule.onNodeWithText("Message").assertIsNotSelected()
    composeRule.onNodeWithText("Feed").assertIsSelected()
  }

  @Test
  fun testEmptyStates(){
    // Test that the list
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      socialViewModel.isConnected = true
      SocialScreen()
    }

    //Feed
    composeRule.onNodeWithTag("ConnectionMissing").assertIsNotDisplayed()
    composeRule.onNodeWithTag("FriendMissing").assertIsDisplayed()

    //Friends
    composeRule.onNodeWithText("Friends").performClick()
    composeRule.onNodeWithTag("FriendMissing").assertIsDisplayed()
    composeRule.onNodeWithTag("ConnectionMissing").assertIsNotDisplayed()

    //Message
    composeRule.onNodeWithText("Message").performClick()
    composeRule.onNodeWithTag("ConnectionMissing").assertIsNotDisplayed()
    composeRule.onNodeWithTag("MessageMissing").assertIsDisplayed()

  }


  @Test
  fun testFilledFeed(){
    // Set the content to the social screen
    composeRule.activity.setContent {
      socialViewModel = hiltViewModel()
      socialViewModel.isConnected = true
      SocialScreen()
    }

    // Set the viewmodel

  }

}
