package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.user.UserLevel
import com.lastaoutdoor.lasta.data.model.user.UserModel
import com.lastaoutdoor.lasta.ui.screen.discover.DiscoveryScreen
import com.lastaoutdoor.lasta.ui.screen.discovery.FilterScreen
import com.lastaoutdoor.lasta.ui.screen.favorites.FavoritesScreen
import com.lastaoutdoor.lasta.ui.screen.moreinfo.MoreInfoScreen
import com.lastaoutdoor.lasta.ui.screen.profile.ProfileScreen
import com.lastaoutdoor.lasta.ui.screen.settings.SettingsScreen
import com.lastaoutdoor.lasta.ui.screen.social.ConversationScreen
import com.lastaoutdoor.lasta.ui.screen.social.FriendProfileScreen
import com.lastaoutdoor.lasta.ui.screen.social.NotificationsScreen
import com.lastaoutdoor.lasta.ui.screen.social.SocialScreen
import com.lastaoutdoor.lasta.ui.screen.social.components.SendMessageDialog
import com.lastaoutdoor.lasta.viewmodel.ConversationViewModel
import com.lastaoutdoor.lasta.viewmodel.MoreInfoScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun MenuNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    modifier: Modifier,
    moreInfoScreenViewModel: MoreInfoScreenViewModel = hiltViewModel()
) {

  // allows traducted roots
  val disc = LocalContext.current.getString(R.string.tab_discover)
  val prof = LocalContext.current.getString(R.string.tab_profile)
  val soc = LocalContext.current.getString(R.string.socials)
  val fav = LocalContext.current.getString(R.string.favs)
  val fil = "Filter"

  NavHost(
      navController = navController,
      route = RootScreen.Main.route,
      modifier = modifier.testTag("MenuNavGraph"),
      startDestination = disc) {
        composable(disc) {
          DiscoveryScreen(navController, moreInfoScreenViewModel = moreInfoScreenViewModel)
        }
        composable(fil) { FilterScreen(navController = navController) }

        composable(fav) { FavoritesScreen(navController) }
        composable(soc) { SocialScreen(navController) }
        composable(prof) {
          ProfileScreen(rootNavController = rootNavController, navController = navController)
        }
        composable(LeafScreen.MoreInfo.route) {
          MoreInfoScreen(
              navController = navController, moreInfoScreenViewModel = moreInfoScreenViewModel)
        }
        composable(LeafScreen.Settings.route) {
          SettingsScreen(rootNavController = rootNavController, navController = navController)
        }
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
              if (conversationViewModel.showSendMessageDialog) {
                SendMessageDialog(
                    hideDialog = { conversationViewModel.hideSendMessageDialog() },
                    send = conversationViewModel::send)
              }
            }
        // The friend profile screen
        composable(
            LeafScreen.FriendProfile.route + "/{friendId}",
            arguments = listOf(navArgument("friendId") { type = NavType.StringType })) {

              // get the friend userModel
              val socialVM: SocialViewModel = hiltViewModel()
              socialVM.refreshFriends()

              val defaultUserModel: UserModel =
                  UserModel(
                      it.arguments?.getString("friendId") ?: "",
                      null,
                      null,
                      null,
                      null,
                      UserLevel.BEGINNER)

              // Create a state for the UserModel with a default value
              val friendUserModel = remember { mutableStateOf(defaultUserModel) }

              // Update the state when the list is updated
              LaunchedEffect(socialVM.friends) {
                val friendId = it.arguments?.getString("friendId") ?: ""
                friendUserModel.value =
                    socialVM.friends.firstOrNull { it.userId == friendId } ?: defaultUserModel
              }

              FriendProfileScreen(friend = friendUserModel.value, navController::popBackStack)
            }

        composable(LeafScreen.Notifications.route) {
          NotificationsScreen(navController = navController)
        }
      }
}
