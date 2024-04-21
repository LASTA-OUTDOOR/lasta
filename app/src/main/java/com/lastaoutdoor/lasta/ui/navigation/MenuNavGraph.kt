package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lastaoutdoor.lasta.ui.screen.activities.MoreInfoScreen
import com.lastaoutdoor.lasta.ui.screen.discovery.DiscoveryScreen
import com.lastaoutdoor.lasta.ui.screen.favorites.FavoritesScreen
import com.lastaoutdoor.lasta.ui.screen.profile.ProfileScreen
import com.lastaoutdoor.lasta.ui.screen.social.SocialScreen
import com.lastaoutdoor.lasta.viewmodel.MoreInfoScreenViewModel

@Composable
fun MenuNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    modifier: Modifier,
    moreInfoScreenViewModel: MoreInfoScreenViewModel = hiltViewModel()
) {
  NavHost(
      navController = navController,
      route = RootScreen.Main.route,
      modifier = modifier.testTag("MenuNavGraph"),
      startDestination = LeafScreen.Discover.route) {
        composable(LeafScreen.Discover.route) {
          DiscoveryScreen(navController, moreInfoScreenViewModel = moreInfoScreenViewModel)
        }
        composable(LeafScreen.Favorites.route) { FavoritesScreen(navController) }
        composable(LeafScreen.Social.route) { SocialScreen() }
        composable(LeafScreen.Profile.route) {
          ProfileScreen(rootNavController = rootNavController)
        }
        composable(LeafScreen.MoreInfo.route) {
          MoreInfoScreen(
              navController = navController, moreInfoScreenViewModel = moreInfoScreenViewModel)
        }
      }
}
