package com.lastaoutdoor.lasta.navigation

import androidx.compose.material.Badge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

data class MenuNavigationItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)

@Composable
fun MenuNavigation(navController: NavHostController) {
  val items =
      listOf(
          MenuNavigationItem(
              route = LeafScreen.Map.route,
              selectedIcon = Icons.Filled.Place,
              unselectedIcon = Icons.Outlined.Place,
              hasNews = false,
          ),
          MenuNavigationItem(
              route = LeafScreen.Discover.route,
              selectedIcon = Icons.Filled.Add,
              unselectedIcon = Icons.Outlined.Add,
              hasNews = false),
          MenuNavigationItem(
              route = LeafScreen.Profile.route,
              selectedIcon = Icons.Filled.AccountCircle,
              unselectedIcon = Icons.Outlined.AccountCircle,
              hasNews = true))

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination

  NavigationBar {
    items.forEach { item ->
      val selected = currentDestination?.route == item.route
      NavigationBarItem(
          selected = selected,
          onClick = {
            navController.navigate(item.route) {
              popUpTo(navController.graph.findStartDestination().id) { saveState = true }
              launchSingleTop = true
              restoreState = true
            }
          },
          label = { Text(item.route) },
          icon = {
            BadgedBox(badge = { if (item.hasNews) Badge() }) {
              Icon(
                  imageVector =
                      if (selected) {
                        item.selectedIcon
                      } else item.unselectedIcon,
                  contentDescription = item.route)
            }
          })
    }
  }
}