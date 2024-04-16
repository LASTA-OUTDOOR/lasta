package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lastaoutdoor.lasta.ui.screen.discovery.DiscoveryScreen
import com.lastaoutdoor.lasta.ui.screen.map.MapScreen
import com.lastaoutdoor.lasta.ui.screen.profile.ProfileScreen

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
        composable(LeafScreen.Map.route) { MapScreen() }
        composable(LeafScreen.Discover.route) { DiscoveryScreen() }
        composable(LeafScreen.Profile.route) {
          ProfileScreen(rootNavController = rootNavController)
        }
      }
}
