package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenViewModel
import com.lastaoutdoor.lasta.viewmodel.MoreInfoScreenViewModel
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
    composable(DestinationRoute.Socials.route) { SocialScreen() }
    composable(DestinationRoute.Profile.route) { ProfileScreen() }

    composable(DestinationRoute.MoreInfo.route) { entry ->
      val moreInfoScreenViewModel: MoreInfoScreenViewModel = entry.sharedViewModel(navController)
      val activityToDisplay = moreInfoScreenViewModel.activityToDisplay.value
      MoreInfoScreen(activityToDisplay, moreInfoScreenViewModel::processDiffText) { navController.navigateUp() }
    }
    composable(DestinationRoute.Filter.route) { FilterScreen { navController.popBackStack() } }

    composable(DestinationRoute.Conversation.route) { ConversationScreen() }
    composable(DestinationRoute.Notifications.route) { NotificationsScreen() }
    composable(DestinationRoute.FriendProfile.route) {
      FriendProfileScreen(friend = null, onBack = {})
    }

    composable(DestinationRoute.Settings.route) { SettingsScreen() }
  }
}
