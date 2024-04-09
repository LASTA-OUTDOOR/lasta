package com.lastaoutdoor.lasta.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lastaoutdoor.lasta.ui.screen.DiscoveryScreen
import com.lastaoutdoor.lasta.ui.screen.MapScreen
import com.lastaoutdoor.lasta.ui.screen.ProfileScreen
import com.lastaoutdoor.lasta.viewmodel.OutdoorActivityViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

@Composable
fun MenuNavGraph(
    navController: NavHostController,
    preferencesViewModel: PreferencesViewModel,
    outdoorActivityViewModel: OutdoorActivityViewModel,
    modifier: Modifier,
    onSignOut: () -> Unit
) {
  NavHost(
      navController = navController,
      route = RootScreen.Main.route,
      modifier = modifier,
      startDestination = LeafScreen.Discover.route) {
        composable(LeafScreen.Map.route) { MapScreen() }
        composable(LeafScreen.Discover.route) { DiscoveryScreen(outdoorActivityViewModel) }
        composable(LeafScreen.Profile.route) {
          ProfileScreen(preferencesViewModel, onSignOut = { onSignOut() })
        }
      }
}
