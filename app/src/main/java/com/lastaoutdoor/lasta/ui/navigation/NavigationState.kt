package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
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
      navController.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
      }
    }
  }
}

@Composable
fun rememberNavigationState(navController: NavHostController = rememberNavController()) =
    remember(navController) { NavigationState(navController) }
