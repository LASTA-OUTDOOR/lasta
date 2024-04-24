package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.screen.activities.MoreInfoScreen
import com.lastaoutdoor.lasta.ui.screen.discovery.DiscoveryScreen
import com.lastaoutdoor.lasta.ui.screen.favorites.FavoritesScreen
import com.lastaoutdoor.lasta.ui.screen.profile.ProfileScreen
import com.lastaoutdoor.lasta.ui.screen.social.AddFriendScreen
import com.lastaoutdoor.lasta.ui.screen.social.NewMessageScreen
import com.lastaoutdoor.lasta.ui.screen.social.NotificationsScreen
import com.lastaoutdoor.lasta.ui.screen.social.SocialScreen
import com.lastaoutdoor.lasta.viewmodel.MoreInfoScreenViewModel

@Composable
fun MenuNavGraph(
    rootNavController: NavHostController,
    navController: NavHostController,
    modifier: Modifier,
    moreInfoScreenViewModel: MoreInfoScreenViewModel = hiltViewModel()
) {
    val disc = LocalContext.current.getString(R.string.tab_discover)
    val prof = LocalContext.current.getString(R.string.tab_profile)
    val soc = LocalContext.current.getString(R.string.socials)
    val fav = LocalContext.current.getString(R.string.favs)
  NavHost(
      navController = navController,
      route = RootScreen.Main.route,
      modifier = modifier.testTag("MenuNavGraph"),
      startDestination = disc) {
        composable(disc) {
          DiscoveryScreen(navController, moreInfoScreenViewModel = moreInfoScreenViewModel)
        }
        composable(fav) { FavoritesScreen(navController) }
        composable(soc) { SocialScreen(navController) }
        composable(prof) {
          ProfileScreen(rootNavController = rootNavController)
        }
        composable(LeafScreen.MoreInfo.route) {
          MoreInfoScreen(
              navController = navController, moreInfoScreenViewModel = moreInfoScreenViewModel)
        }
        composable(LeafScreen.AddFriend.route) { AddFriendScreen(navController = navController) }
        composable(LeafScreen.NewMessage.route) { NewMessageScreen(navController = navController) }
        composable(LeafScreen.Notifications.route) {
          NotificationsScreen(navController = navController)
        }
      }
}
