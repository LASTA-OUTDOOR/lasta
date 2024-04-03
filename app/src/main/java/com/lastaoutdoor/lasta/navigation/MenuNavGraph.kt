package com.lastaoutdoor.lasta.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lastaoutdoor.lasta.ui.screen.DiscoveryScreen
import com.lastaoutdoor.lasta.ui.screen.MapScreen
import com.lastaoutdoor.lasta.ui.screen.ProfileScreen

@Composable
fun MenuNavGraph(navController: NavHostController, modifier: Modifier, onSignOut: () -> Unit) {
  NavHost(
      navController = navController,
      route = RootScreen.Main.route,
      startDestination = LeafScreen.Map.route,
      modifier = modifier) {
        composable(LeafScreen.Map.route) { MapScreen() }
        composable(LeafScreen.Discover.route) { DiscoveryScreen() }
        composable(LeafScreen.Profile.route) { ProfileScreen(null, onSignOut = { onSignOut() }) }
      }
}
