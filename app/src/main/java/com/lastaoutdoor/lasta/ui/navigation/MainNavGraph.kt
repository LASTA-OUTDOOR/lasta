package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.screen.discover.DiscoverScreen
import com.lastaoutdoor.lasta.ui.screen.discover.FilterScreen
import com.lastaoutdoor.lasta.ui.screen.favorites.FavoritesScreen
import com.lastaoutdoor.lasta.ui.screen.moreinfo.MoreInfoScreen
import com.lastaoutdoor.lasta.ui.screen.profile.ProfileScreen
import com.lastaoutdoor.lasta.ui.screen.settings.SettingsScreen
import com.lastaoutdoor.lasta.ui.screen.social.ConversationScreen
import com.lastaoutdoor.lasta.ui.screen.social.FriendProfileScreen
import com.lastaoutdoor.lasta.ui.screen.social.NotificationsScreen
import com.lastaoutdoor.lasta.ui.screen.social.SocialScreen
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.ConversationViewModel
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.MoreInfoScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.ProfileScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel
import com.lastaoutdoor.lasta.viewmodel.WeatherViewModel

fun NavGraphBuilder.addMainNavGraph(navController: NavHostController) {
  navigation(startDestination = DestinationRoute.Discover.route, route = BaseRoute.Main.route) {
    composable(DestinationRoute.Discover.route) { entry ->
      val discoverScreenViewModel: DiscoverScreenViewModel = hiltViewModel(entry)
      val moreInfoScreenViewModel: MoreInfoScreenViewModel = entry.sharedViewModel(navController)

      val activities = discoverScreenViewModel.activities.value
      val screen = discoverScreenViewModel.screen.collectAsState().value
      val range = discoverScreenViewModel.range.collectAsState().value
      val localities = discoverScreenViewModel.localities
      val selectedLocality = discoverScreenViewModel.selectedLocality.collectAsState().value

      DiscoverScreen(
          activities,
          screen,
          range,
          localities,
          selectedLocality,
          discoverScreenViewModel::fetchBikingActivities,
          discoverScreenViewModel::setScreen,
          discoverScreenViewModel::setRange,
          discoverScreenViewModel::setSelectedLocality,
          { navController.navigate(DestinationRoute.Filter.route) },
          { navController.navigate(DestinationRoute.MoreInfo.route) },
          moreInfoScreenViewModel::changeActivityToDisplay)
    }
    composable(DestinationRoute.Favorites.route) { entry ->
      val weatherViewModel: WeatherViewModel = hiltViewModel(entry)
      val weather = weatherViewModel.weather.observeAsState().value ?: return@composable
      FavoritesScreen({ navController.navigate(DestinationRoute.MoreInfo.route) }, weather)
    }
    composable(DestinationRoute.Socials.route) { entry ->
      val socialViewModel: SocialViewModel = entry.sharedViewModel(navController)
      val isConnected = socialViewModel.isConnected.collectAsState().value
      SocialScreen(
          socialViewModel.hasFriendRequest,
          socialViewModel.topButton,
          socialViewModel.topButtonIcon,
          socialViewModel.topButtonOnClick,
          socialViewModel::refreshFriendRequests,
        {navController.navigate(DestinationRoute.Notifications.route)},
          isConnected,
          socialViewModel.friends,
          socialViewModel.messages,
          socialViewModel.latestFriendActivities,
          socialViewModel.displayAddFriendDialog,
          socialViewModel.friendRequestFeedback,
          socialViewModel.displayFriendPicker,
          socialViewModel::refreshMessages,
          socialViewModel::clearFriendRequestFeedback,
          socialViewModel::hideAddFriendDialog,
          socialViewModel::requestFriend,
          socialViewModel::refreshFriends,
          socialViewModel::showTopButton,
          socialViewModel::hideTopButton,
          socialViewModel::displayAddFriendDialog,
          socialViewModel::displayFriendPicker,
          socialViewModel::hideFriendPicker,
          socialViewModel::displayFriendPicker,
        {userId: String -> navController.navigate(DestinationRoute.Conversation.route + "/$userId") },
        {userId: String -> navController.navigate(DestinationRoute.Conversation.route + "/$userId") },)
    }
    composable(DestinationRoute.Profile.route) { entry ->
      val profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(entry)

      val activities by profileScreenViewModel.filteredActivities.collectAsState()
      val timeFrame by profileScreenViewModel.timeFrame.collectAsState()
      val sport by profileScreenViewModel.sport.collectAsState()
      val isCurrentUser by profileScreenViewModel.isCurrentUser.collectAsState()
      ProfileScreen(
          activities,
          timeFrame,
          sport,
          isCurrentUser,
          profileScreenViewModel::setTimeFrame,
          profileScreenViewModel::setSport,
          profileScreenViewModel::fetchActivities)
    }

    composable(DestinationRoute.MoreInfo.route) { entry ->
      val moreInfoScreenViewModel: MoreInfoScreenViewModel = entry.sharedViewModel(navController)
      val activityToDisplay = moreInfoScreenViewModel.activityToDisplay.value
      MoreInfoScreen(activityToDisplay, moreInfoScreenViewModel::processDiffText) {
        navController.navigateUp()
      }
    }
    composable(DestinationRoute.Filter.route) { FilterScreen { navController.popBackStack() } }

    composable(
        DestinationRoute.Conversation.route + "/{userId}",
        arguments = listOf(navArgument("userId") { type = NavType.StringType })) { entry ->
          val conversationViewModel: ConversationViewModel = hiltViewModel(entry)
          conversationViewModel.updateFriendUserId(entry.arguments?.getString("userId") ?: "")
          val conversation = conversationViewModel.conversation
          ConversationScreen(
              conversation,
              conversationViewModel::updateConversation,
              conversationViewModel.userId,
              conversationViewModel.friendUserId,
              conversationViewModel::showSendMessageDialog,
              navController::navigateUp)
        }
    composable(DestinationRoute.Notifications.route) { entry ->
      val socialViewModel: SocialViewModel = entry.sharedViewModel(navController)
      val isConnected: ConnectionState = socialViewModel.isConnected.collectAsState().value
      NotificationsScreen(
          isConnected,
          socialViewModel.friendRequests,
          socialViewModel::acceptFriend,
          socialViewModel::declineFriend) {
            navController.navigateUp()
          }
    }
    composable(
        DestinationRoute.FriendProfile.route + "/{friendId}",
        arguments = listOf(navArgument("friendId") { type = NavType.StringType })) { entry ->
          val socialViewModel: SocialViewModel = entry.sharedViewModel(navController)
          socialViewModel.refreshFriends()
          val friendId = entry.arguments?.getString("friendId") ?: ""
          val defaultUserModel: UserModel = UserModel(friendId)

          // Create a state for the UserModel with a default value
          val friendUserModel = remember { mutableStateOf(defaultUserModel) }

          // Update the state when the list is updated
          LaunchedEffect(socialViewModel.friends) {
            friendUserModel.value =
                socialViewModel.friends.firstOrNull { it.userId == friendId } ?: defaultUserModel
          }
          FriendProfileScreen(friendUserModel.value, navController::navigateUp)
        }

    composable(DestinationRoute.Settings.route) { SettingsScreen() }
  }
}
