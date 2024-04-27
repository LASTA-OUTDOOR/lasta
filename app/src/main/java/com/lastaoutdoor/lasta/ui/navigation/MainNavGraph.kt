package com.lastaoutdoor.lasta.ui.navigation

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

fun NavGraphBuilder.addMainNavGraph(navController: NavHostController) {
  navigation(startDestination = DestinationRoute.Discover.route, route = BaseRoute.Main.route) {
    composable(DestinationRoute.Discover.route) { DiscoverScreen() }
    composable(DestinationRoute.Favorites.route) { FavoritesScreen() }
    composable(DestinationRoute.Socials.route) { SocialScreen() }
    composable(DestinationRoute.Profile.route) { ProfileScreen() }

    composable(DestinationRoute.MoreInfo.route) { MoreInfoScreen() }
    composable(DestinationRoute.Filter.route) { FilterScreen() }

    composable(DestinationRoute.Conversation.route) { ConversationScreen() }
    composable(DestinationRoute.Notifications.route) { NotificationsScreen() }
    composable(DestinationRoute.FriendProfile.route) {
      FriendProfileScreen(friend = null, onBack = {})
    }

    composable(DestinationRoute.Settings.route) { SettingsScreen() }
  }
}
