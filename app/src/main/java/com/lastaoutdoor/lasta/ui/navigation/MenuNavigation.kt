package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.ui.screen.DiscoveryScreen
import com.lastaoutdoor.lasta.ui.screen.MapScreen
import com.lastaoutdoor.lasta.ui.screen.ProfileScreen

data class MenuNavigationItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)

@Composable
fun MenuNavigation() {
  val items =
      listOf(
          MenuNavigationItem(
              route = "Map",
              selectedIcon = Icons.Filled.Place,
              unselectedIcon = Icons.Outlined.Place,
              hasNews = false,
          ),
          MenuNavigationItem(
              route = "Discover",
              selectedIcon = Icons.Filled.Add,
              unselectedIcon = Icons.Outlined.Add,
              hasNews = false),
          MenuNavigationItem(
              route = "Profile",
              selectedIcon = Icons.Filled.AccountCircle,
              unselectedIcon = Icons.Outlined.AccountCircle,
              hasNews = true))

  val navMenuController = rememberNavController()
  var selectedIndex by remember { mutableIntStateOf(0) }

  Scaffold(
      bottomBar = {
        NavigationBar {
          items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                  selectedIndex = index
                  navMenuController.navigate(item.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navMenuController.graph.findStartDestination().id) { saveState = true }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                  }
                },
                label = { Text(item.route) },
                icon = {
                  BadgedBox(badge = { if (item.hasNews) Badge() }) {
                    Icon(
                        imageVector =
                            if (selectedIndex == index) {
                              item.selectedIcon
                            } else item.unselectedIcon,
                        contentDescription = item.route)
                  }
                })
          }
        }
      }) { innerPadding ->
        NavHost(navMenuController, startDestination = "map", Modifier.padding(innerPadding)) {
          composable("map") { MapScreen() }
          composable("discover") { DiscoveryScreen() }
          composable("profile") { ProfileScreen(null, onSignOut = {}, goToPreferences = {}) }
        }
      }
}
