package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Stable
class NavigationState(val navController: NavHostController) {
  val bottomBarTabs = BottomBarTab.values()
  private val bottomBarRoutes = bottomBarTabs.map { it.route }

  val shouldShowBottomBar: Boolean
    @Composable
    get() =
        navController.currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

  val currentRoute: String?
    get() = navController.currentDestination?.route

  fun navigateToBottomBarRoute(route: String) {
    if (route != currentRoute) {
      if (currentRoute != DestinationRoute.Discover.route)
          navController.popBackStack(
              DestinationRoute.Discover.route, inclusive = false, saveState = true)
      navController.navigate(route)
    }
  }
}

@Composable
fun rememberNavigationState(navController: NavHostController = rememberNavController()) =
    remember(navController) { NavigationState(navController) }
