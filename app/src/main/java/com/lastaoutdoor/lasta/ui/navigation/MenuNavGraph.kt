package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lastaoutdoor.lasta.ui.screen.activities.MoreInfoScreen
import com.lastaoutdoor.lasta.ui.screen.discovery.DiscoveryScreen
import com.lastaoutdoor.lasta.ui.screen.favorites.FavoritesScreen
import com.lastaoutdoor.lasta.ui.screen.profile.ProfileScreen
import com.lastaoutdoor.lasta.ui.screen.social.ConversationScreen
import com.lastaoutdoor.lasta.ui.screen.social.NotificationsScreen
import com.lastaoutdoor.lasta.ui.screen.social.SocialScreen
import com.lastaoutdoor.lasta.ui.screen.social.components.SendMessageDialog
import com.lastaoutdoor.lasta.viewmodel.ConversationViewModel

@Composable
fun MenuNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    modifier: Modifier
) {
  NavHost(
      navController = navController,
      route = RootScreen.Main.route,
      modifier = modifier.testTag("MenuNavGraph"),
      startDestination = LeafScreen.Discover.route) {
        composable(LeafScreen.Discover.route) { DiscoveryScreen(navController) }
        composable(LeafScreen.Favorites.route) { FavoritesScreen(navController) }
        composable(LeafScreen.Social.route) { SocialScreen(navController) }
        composable(LeafScreen.Profile.route) {
          ProfileScreen(rootNavController = rootNavController)
        }
        composable(LeafScreen.MoreInfo.route) { MoreInfoScreen(navController = navController) }
        composable(
            LeafScreen.Conversation.route + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })) {

              // View Model
              val conversationViewModel = hiltViewModel<ConversationViewModel>()

              // Friend User Id
              conversationViewModel.updateFriendUserId(it.arguments?.getString("userId") ?: "")

              ConversationScreen(
                  navController = navController,
                  conversationModel = conversationViewModel.conversation,
                  refresh = { conversationViewModel.updateConversation() },
                  userId = conversationViewModel.userId,
                  friendId = conversationViewModel.friendUserId,
                  showSendDialog = { conversationViewModel.showSendMessageDialog() })

              // Send Message Dialog
              if(conversationViewModel.showSendMessageDialog){
                  SendMessageDialog(
                      hideDialog = { conversationViewModel.hideSendMessageDialog() }, send = conversationViewModel::send)
              }
            }

        composable(LeafScreen.Notifications.route) {
          NotificationsScreen(navController = navController)
        }
      }
}
