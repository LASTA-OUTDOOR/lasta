@file:OptIn(ExperimentalAnimationApi::class)

package com.lastaoutdoor.lasta.ui.navigation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.get
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.user.Language
import com.lastaoutdoor.lasta.models.user.UserActivitiesLevel
import com.lastaoutdoor.lasta.models.user.UserLevel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.screen.discover.DiscoverScreen
import com.lastaoutdoor.lasta.ui.screen.discover.FilterScreen
import com.lastaoutdoor.lasta.ui.screen.discover.components.RangeSearchComposable
import com.lastaoutdoor.lasta.ui.screen.favorites.FavoritesScreen
import com.lastaoutdoor.lasta.ui.screen.moreinfo.MoreInfoScreen
import com.lastaoutdoor.lasta.ui.screen.profile.ProfileScreen
import com.lastaoutdoor.lasta.ui.screen.settings.SettingsScreen
import com.lastaoutdoor.lasta.ui.screen.social.ConversationScreen
import com.lastaoutdoor.lasta.ui.screen.social.FriendProfileScreen
import com.lastaoutdoor.lasta.ui.screen.social.NotificationsScreen
import com.lastaoutdoor.lasta.ui.screen.social.SocialScreen
import com.lastaoutdoor.lasta.ui.screen.tracking.TrackingActivity
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.PermissionManager
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.ConnectivityViewModel
import com.lastaoutdoor.lasta.viewmodel.ConversationViewModel
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenCallBacks
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.FavoritesScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.MoreInfoScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import com.lastaoutdoor.lasta.viewmodel.ProfileScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel
import com.lastaoutdoor.lasta.viewmodel.WeatherViewModel

@SuppressLint("StateFlowValueCalledInComposition", "RestrictedApi", "ResourceType")
fun NavGraphBuilder.addMainNavGraph(navController: NavHostController) {

  navigation(
      startDestination = DestinationRoute.Discover.route,
      route = BaseRoute.Main.route + "/{activityId}") {
        argument("activityId") { type = NavType.StringType }

        // Discover Screen
        composable(DestinationRoute.Discover.route) { entry ->
          val discoverScreenViewModel: DiscoverScreenViewModel =
              entry.sharedViewModel(navController)
          val moreInfoScreenViewModel: MoreInfoScreenViewModel =
              entry.sharedViewModel(navController)
          val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)
          val favorites = preferencesViewModel.favorites.collectAsState(initial = emptyList()).value
          val weatherViewModel: WeatherViewModel = entry.sharedViewModel(navController)
          val weather = weatherViewModel.weather.observeAsState().value

          val discoverScreenState: DiscoverScreenState =
              discoverScreenViewModel.state.collectAsState().value
          val discoverScreenCallBacks: DiscoverScreenCallBacks = discoverScreenViewModel.callbacks
          val isOnline = discoverScreenViewModel.isConnected.collectAsState().value
          PermissionManager(discoverScreenViewModel::updatePermission)

          // Navigate to the more info screen if the app is launched with a deep link
          val activityId = entry.arguments?.getString("activityId") ?: ""

          if (activityId.isNotEmpty() && activityId != "DEFAULT") {

            moreInfoScreenViewModel.changeActivityToDisplayByID(activityId)
            navController.navigate(DestinationRoute.MoreInfo.route)

            // tell the viewmodel it was opened by a share link
            moreInfoScreenViewModel.openByShareLink.value = true

            // reset the activityId to DEFAULT
            entry.arguments?.clear()
            entry.arguments?.putString("activityId", "DEFAULT")
          }
          DiscoverScreen(
              discoverScreenState,
              discoverScreenCallBacks,
              favorites,
              preferencesViewModel::flipFavorite,
              { navController.navigate(DestinationRoute.Filter.route) },
              { navController.navigate(DestinationRoute.MoreInfo.route) },
              { navController.navigate(DestinationRoute.RangeSearch.route) },
              moreInfoScreenViewModel::changeActivityToDisplay,
              weatherViewModel::changeLocOfWeather,
              weather,
              isOnline)
        }

        // Favorites Screen
        composable(DestinationRoute.Favorites.route) { entry ->
          val discoverScreenViewModel: DiscoverScreenViewModel =
              entry.sharedViewModel(navController)
          val favoritesScreenViewModel: FavoritesScreenViewModel = hiltViewModel(entry)
          val moreInfoScreenViewModel: MoreInfoScreenViewModel =
              entry.sharedViewModel(navController)

          val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)
          val isLoading = favoritesScreenViewModel.isLoading.collectAsState().value
          val favorites = favoritesScreenViewModel.favorites.collectAsState().value
          val centerPoint =
              discoverScreenViewModel.state.collectAsState().value.selectedLocality.second
          val favoriteIds = favoritesScreenViewModel.favoritesIds.collectAsState().value
          val weatherViewModel: WeatherViewModel = entry.sharedViewModel(navController)
          val isOnline = discoverScreenViewModel.isConnected.collectAsState().value
          FavoritesScreen(
              isLoading,
              favorites,
              centerPoint,
              favoriteIds,
              moreInfoScreenViewModel::changeActivityToDisplay,
              weatherViewModel::changeLocOfWeather,
              preferencesViewModel::flipFavorite,
              favoritesScreenViewModel::fetchFavorites,
              { navController.navigate(DestinationRoute.MoreInfo.route) },
              isOnline)
        }

        // Social Screen
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
              socialViewModel.friendSuggestions,
              socialViewModel.displayFriendPicker,
              socialViewModel::refreshMessages,
              socialViewModel::clearFriendRequestFeedback,
              socialViewModel::hideAddFriendDialog,
              socialViewModel::fetchFriendsSuggestions,
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
              socialViewModel::refreshFriendsActivities)
        }

        // Profile Screen
        composable(DestinationRoute.Profile.route) { entry ->
          val profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(entry)
          val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)
          val connectivityViewModel: ConnectivityViewModel = hiltViewModel(entry)
          val activities by profileScreenViewModel.filteredActivities.collectAsState()
          val timeFrame by profileScreenViewModel.timeFrame.collectAsState()
          val sport by profileScreenViewModel.sport.collectAsState()
          val isCurrentUser by profileScreenViewModel.isCurrentUser.collectAsState()
          val user = preferencesViewModel.user.collectAsState(initial = UserModel("")).value
          val isOnline = connectivityViewModel.connectionState.collectAsState().value
          ProfileScreen(
              activities,
              timeFrame,
              sport,
              isCurrentUser,
              user,
              preferencesViewModel::updateDescription,
              profileScreenViewModel::setSport,
              profileScreenViewModel::setTimeFrame,
              { navController.navigate(DestinationRoute.Settings.route) },
              isOnline = isOnline)
        }

        // More Info Screen
        composable(route = DestinationRoute.MoreInfo.route) { entry ->

          // check if user is logged in
          val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)
          val currentUser = preferencesViewModel.user.collectAsState(initial = UserModel("")).value
          val favorites = preferencesViewModel.favorites.collectAsState(initial = emptyList()).value

          // View models
          val discoverScreenViewModel: DiscoverScreenViewModel =
              entry.sharedViewModel(navController)
          val moreInfoScreenViewModel: MoreInfoScreenViewModel =
              entry.sharedViewModel(navController)
          val conversationViewModel: ConversationViewModel = entry.sharedViewModel(navController)
          val socialViewModel: SocialViewModel = entry.sharedViewModel(navController)

          val activityToDisplay = moreInfoScreenViewModel.activityToDisplay.value
          val usersList = moreInfoScreenViewModel.usersList.collectAsState().value
          val weatherViewModel: WeatherViewModel = entry.sharedViewModel(navController)
          val weather = weatherViewModel.weather.observeAsState().value

          val discoverScreenState: DiscoverScreenState =
              discoverScreenViewModel.state.collectAsState().value
          val discoverScreenCallBacks: DiscoverScreenCallBacks = discoverScreenViewModel.callbacks

          val weatherForecast = weatherViewModel.getForecast()
          val dateWeatherForecast = weatherViewModel.getForecastDate()
          val isOnline = discoverScreenViewModel.isConnected.collectAsState().value
          val openedByShareLink = moreInfoScreenViewModel.openByShareLink.collectAsState().value

          val context = LocalContext.current

          MoreInfoScreen(
              activityToDisplay,
              discoverScreenState,
              discoverScreenCallBacks,
              moreInfoScreenViewModel::goToMarker,
              usersList,
              moreInfoScreenViewModel::getUserModels,
              moreInfoScreenViewModel::writeNewRating,
              moreInfoScreenViewModel::updateDifficulty,
              currentUser,
              { message: String, friendId: String
                -> // send the message + navigate to the conversation
                conversationViewModel.shareActivityToFriend(message, friendId)
                navController.navigate(DestinationRoute.Conversation.route + "/$friendId")
              },
              socialViewModel.friends,
              weather,
              favorites,
              weatherForecast,
              dateWeatherForecast,
              preferencesViewModel::flipFavorite,
              {
                if (openedByShareLink) {
                  moreInfoScreenViewModel.openByShareLink.value = false
                  navController.navigate(DestinationRoute.Discover.route)
                } else {
                  navController.navigateUp()
                }
              },
              { context.startActivity(Intent(context, TrackingActivity::class.java)) },
              moreInfoScreenViewModel::downloadActivity,
              isOnline,
              weatherViewModel::fetchWeatherWithUserLoc,
          )
        }

        // Filter Screen
        composable(DestinationRoute.Filter.route) { entry ->
          val discoverScreenViewModel: DiscoverScreenViewModel =
              entry.sharedViewModel(navController)

          FilterScreen(
              discoverScreenViewModel.state,
              discoverScreenViewModel.callbacks,
          ) {
            navController.popBackStack()
          }
        }

        // Conversation Screen
        composable(
            DestinationRoute.Conversation.route + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })) { entry ->
              val conversationViewModel: ConversationViewModel = hiltViewModel(entry)
              conversationViewModel.updateFriendUserId(entry.arguments?.getString("userId") ?: "")
              val moreInfoScreenViewModel: MoreInfoScreenViewModel =
                  entry.sharedViewModel(navController)
              val conversation = conversationViewModel.conversation
              val isOnline = moreInfoScreenViewModel.isConnected.collectAsState().value
              ConversationScreen(
                  conversation,
                  conversationViewModel::updateConversation,
                  moreInfoScreenViewModel::changeActivityToDisplayByID,
                  conversationViewModel.user.value,
                  conversationViewModel.friend.value,
                  conversationViewModel::send,
                  isOnline = isOnline,
                  navigateToMoreInfo = navController::navigateUp,
                  navigateBack = { navController.navigate(DestinationRoute.MoreInfo.route) })
            }

        // Notifications Screen
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

        // Friend Profile Screen
        composable(DestinationRoute.FriendProfile.route + "/{friendId}") { entry ->
          val profileScreenViewModel: ProfileScreenViewModel = hiltViewModel(entry)
          val connectionViewModel: ConnectivityViewModel = hiltViewModel(entry)
          // Update the user to display
          profileScreenViewModel.updateUser(entry.arguments?.getString("friendId") ?: "")

          val activities by profileScreenViewModel.filteredActivities.collectAsState()
          val timeFrame by profileScreenViewModel.timeFrame.collectAsState()
          val sport by profileScreenViewModel.sport.collectAsState()
          val isCurrentUser by profileScreenViewModel.isCurrentUser.collectAsState()
          val user = profileScreenViewModel.user.collectAsState().value
          val isOnline = connectionViewModel.connectionState.collectAsState().value

          FriendProfileScreen(
              activities = activities,
              timeFrame = timeFrame,
              sport = sport,
              isCurrentUser = isCurrentUser,
              user = user,
              setSport = profileScreenViewModel::setSport,
              setTimeFrame = profileScreenViewModel::setTimeFrame,
              navigateToSettings = { navController.navigate(DestinationRoute.Settings.route) },
              onBack = { navController.navigateUp() },
              isOnline = isOnline)
        }

        // Settings Screen
        composable(DestinationRoute.Settings.route) { entry ->
          val authViewModel: AuthViewModel = entry.sharedViewModel(navController)
          val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)
          val language =
              preferencesViewModel.language.collectAsState(initial = Language.ENGLISH).value
          val prefActivity =
              preferencesViewModel.prefActivity.collectAsState(ActivityType.HIKING).value
          val levels =
              preferencesViewModel.levels
                  .collectAsState(
                      initial =
                          UserActivitiesLevel(
                              UserLevel.BEGINNER, UserLevel.BEGINNER, UserLevel.BEGINNER))
                  .value
          SettingsScreen(
              language,
              prefActivity,
              levels,
              preferencesViewModel::updateLanguage,
              preferencesViewModel::updatePrefActivity,
              preferencesViewModel::updateClimbingLevel,
              preferencesViewModel::updateHikingLevel,
              preferencesViewModel::updateBikingLevel,
              { navController.navigateUp() },
              {
                preferencesViewModel.clearPreferences()
                preferencesViewModel.updateIsLoggedIn(false)
                authViewModel.signOut()
                // Pop backstack up to and including the main route, so that the user cannot press
                // back
                // button to get back in the app as a "phantom user"
                navController.popBackStack(BaseRoute.Main.route + "/DEFAULT", true)
                navController.navigate(BaseRoute.Login.route)
              },
              {
                authViewModel.deleteAccount()
                preferencesViewModel.clearPreferences()
                preferencesViewModel.updateIsLoggedIn(false)
                navController.popBackStack()
                // Pop backstack up to and including the main route, so that the user cannot press
                // back
                // button to get back in the app as a "phantom user"
                navController.popBackStack(BaseRoute.Main.route + "/DEFAULT", true)
                navController.navigate(BaseRoute.Login.route)
              })
        }

        // Range Search Screen
        composable(DestinationRoute.RangeSearch.route) { entry ->
          val discoverScreenViewModel: DiscoverScreenViewModel =
              entry.sharedViewModel(navController)
          val discoverScreenState: DiscoverScreenState =
              discoverScreenViewModel.state.collectAsState().value
          val discoverScreenCallBacks: DiscoverScreenCallBacks = discoverScreenViewModel.callbacks
          RangeSearchComposable(
              { navController.navigateUp() }, discoverScreenState, discoverScreenCallBacks)
        }
      }
}
