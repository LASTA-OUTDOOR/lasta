package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.viewmodel.MoreInfoScreenViewModel

@Composable
fun MenuNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    modifier: Modifier,
    moreInfoScreenViewModel: MoreInfoScreenViewModel = hiltViewModel()
) {
  /*

  // allows traducted roots
  val disc = LocalContext.current.getString(R.string.tab_discover)
  val prof = LocalContext.current.getString(R.string.tab_profile)
  val soc = LocalContext.current.getString(R.string.socials)
  val fav = LocalContext.current.getString(R.string.favs)
  val fil = "Filter"

  NavHost(
      navController = navController,
      route = BaseRoute.Main.route,
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
        composable(DestinationRoute.MoreInfo.route) {
          MoreInfoScreen(
              navController = navController, moreInfoScreenViewModel = moreInfoScreenViewModel)
        }
        composable(DestinationRoute.Settings.route) {
          SettingsScreen(rootNavController = rootNavController, navController = navController)
        }
        composable(
            DestinationRoute.Conversation.route + "/{userId}",
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
            DestinationRoute.FriendProfile.route + "/{friendId}",
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

        composable(DestinationRoute.Notifications.route) {
          NotificationsScreen(navController = navController)
        }
      }*/
}
