package com.lastaoutdoor.lasta.ui.navigation

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Stable
class NavigationState(val navController: NavHostController, private val resources: Resources) {
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

/**
 * A composable function that returns the [Resources]. It will be recomposed when `Configuration`
 * gets updated.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
  LocalConfiguration.current
  return LocalContext.current.resources
}

@Composable
fun rememberNavigationState(
    navController: NavHostController = rememberNavController(),
    resources: Resources = resources()
) = remember(navController, resources) { NavigationState(navController, resources) }
