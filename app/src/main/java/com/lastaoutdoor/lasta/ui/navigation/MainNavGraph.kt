package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
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
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.ConversationViewModel
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.MapViewModel
import com.lastaoutdoor.lasta.viewmodel.MoreInfoScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import com.lastaoutdoor.lasta.viewmodel.ProfileScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel
import com.lastaoutdoor.lasta.viewmodel.WeatherViewModel

fun NavGraphBuilder.addMainNavGraph(navController: NavHostController) {
  navigation(startDestination = DestinationRoute.Discover.route, route = BaseRoute.Main.route) {
    composable(DestinationRoute.Discover.route) { entry ->
      val discoverScreenViewModel: DiscoverScreenViewModel = hiltViewModel(entry)
      val moreInfoScreenViewModel: MoreInfoScreenViewModel = entry.sharedViewModel(navController)
      val mapViewModel: MapViewModel = entry.sharedViewModel(navController)
      val activities = discoverScreenViewModel.activities.value
      val screen = discoverScreenViewModel.screen.collectAsState().value
      val range = discoverScreenViewModel.range.collectAsState().value
      val localities = discoverScreenViewModel.localities
      val selectedLocality = discoverScreenViewModel.selectedLocality.collectAsState().value
      val weatherViewModel: WeatherViewModel = hiltViewModel(entry)
      val weather = weatherViewModel.weather.observeAsState().value
      val mapState = mapViewModel.state.collectAsState().value
      val initialZoom = mapViewModel.initialZoom
      val initialPosition = mapViewModel.initialPosition
      val selectedZoom = mapViewModel.selectedZoom

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
          moreInfoScreenViewModel::changeActivityToDisplay,
          weather,
          mapState,
          mapViewModel::updatePermission,
          initialPosition,
          initialZoom,
          mapViewModel::updateMarkers,
          mapViewModel::updateSelectedMarker,
          mapViewModel::clearSelectedItinerary,
          selectedZoom,
          mapViewModel::updateSelectedItinerary)
    }
    composable(DestinationRoute.Favorites.route) { entry ->
      val weatherViewModel: WeatherViewModel = hiltViewModel(entry)
      val weather = weatherViewModel.weather.observeAsState().value
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
          { navController.navigate(DestinationRoute.Notifications.route) },
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
          { userId: String ->
            navController.navigate(DestinationRoute.Conversation.route + "/$userId")
          },
          { userId: String ->
            navController.navigate(DestinationRoute.FriendProfile.route + "/$userId")
          },
      )
    }
    composable(DestinationRoute.Profile.route) { entry ->
      val profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(entry)
      val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)

      val activities by profileScreenViewModel.filteredActivities.collectAsState()
      val timeFrame by profileScreenViewModel.timeFrame.collectAsState()
      val sport by profileScreenViewModel.sport.collectAsState()
      val isCurrentUser by profileScreenViewModel.isCurrentUser.collectAsState()
      val user = preferencesViewModel.user.collectAsState(initial = UserModel("")).value
      ProfileScreen(
          activities,
          timeFrame,
          sport,
          isCurrentUser,
          user,
          preferencesViewModel::updateDescription,
          profileScreenViewModel::setSport,
          profileScreenViewModel::setTimeFrame) {
            navController.navigate(DestinationRoute.Settings.route)
          }
    }

    composable(DestinationRoute.MoreInfo.route) { entry ->
      val moreInfoScreenViewModel: MoreInfoScreenViewModel = entry.sharedViewModel(navController)
      val activityToDisplay = moreInfoScreenViewModel.activityToDisplay.value
      val weatherViewModel: WeatherViewModel = hiltViewModel(entry)
      val weather = weatherViewModel.weather.observeAsState().value
      MoreInfoScreen(activityToDisplay, moreInfoScreenViewModel::processDiffText, weather) {
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
    composable(DestinationRoute.FriendProfile.route + "/{friendId}") { entry ->
      val profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(entry)
      val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)

      // Update the user to display
      println("FriendProfileScreen: ${entry.arguments?.getString("friendId")}")
      profileScreenViewModel.updateUser(entry.arguments?.getString("friendId") ?: "")

      val activities by profileScreenViewModel.filteredActivities.collectAsState()
      val timeFrame by profileScreenViewModel.timeFrame.collectAsState()
      val sport by profileScreenViewModel.sport.collectAsState()
      val isCurrentUser by profileScreenViewModel.isCurrentUser.collectAsState()
      val user = preferencesViewModel.user.collectAsState(initial = UserModel("")).value

      FriendProfileScreen(
          activities = activities,
          timeFrame = timeFrame,
          sport = sport,
          isCurrentUser = isCurrentUser,
          user = user,
          updateDescription = preferencesViewModel::updateDescription,
          setSport = profileScreenViewModel::setSport,
          setTimeFrame = profileScreenViewModel::setTimeFrame,
          navigateToSettings = { navController.navigate(DestinationRoute.Settings.route) },
          onBack = { navController.navigateUp() })
    }

    composable(DestinationRoute.Settings.route) { entry ->
      val authViewModel: AuthViewModel = hiltViewModel()
      val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)
      val language = preferencesViewModel.language.collectAsState(initial = Language.ENGLISH).value
      val prefActivity = preferencesViewModel.prefActivity.collectAsState(ActivityType.HIKING).value
      SettingsScreen(
          language,
          prefActivity,
          preferencesViewModel::updateLanguage,
          preferencesViewModel::updatePrefActivity,
          { navController.navigateUp() },
          {
            preferencesViewModel.clearPreferences()
            preferencesViewModel.updateIsLoggedIn(false)
            authViewModel.signOut()
            navController.popBackStack()
            navController.navigate(BaseRoute.Login.route)
          })
    }
  }
}
