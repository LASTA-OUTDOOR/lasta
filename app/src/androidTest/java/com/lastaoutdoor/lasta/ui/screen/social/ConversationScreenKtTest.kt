package com.lastaoutdoor.lasta.ui.screen.social

import com.lastaoutdoor.lasta.di.AppModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules

@HiltAndroidTest
@UninstallModules(AppModule::class)
class ConversationScreenKtTest {
  /*@get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)

  // Create a compose rule
  @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

  // Viewmodel and repository
  private lateinit var convViewModel: ConversationViewModel

  // Navigation controller
  private lateinit var navController: NavController

  // Set up the test
  @Before
  fun setUp() {
    hiltRule.inject()
    // set up the navigation controller
    navController = NavHostController(composeRule.activity)
  }

  // Default layout of the page
  @Test
  fun initialState() {
    // Set the content to the social screen
    composeRule.activity.setContent {
      convViewModel = hiltViewModel()
      ConversationScreen(
          navController,
          conversationModel = convViewModel.conversation,
          refresh = { convViewModel.updateConversation() },
          "",
          "",
          { convViewModel.showSendMessageDialog() })
    }

    // Header (title)
    composeRule.onNodeWithTag("Dummy").assertDoesNotExist()
  }*/
}
