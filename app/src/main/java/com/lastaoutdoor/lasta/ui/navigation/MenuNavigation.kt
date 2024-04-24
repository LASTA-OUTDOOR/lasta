package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lastaoutdoor.lasta.R

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
              route = LocalContext.current.getString(R.string.tab_discover),
              selectedIcon = Icons.Filled.Search,
              unselectedIcon = Icons.Outlined.Search,
              hasNews = false),
          MenuNavigationItem(
              route = LocalContext.current.getString(R.string.favs),
              selectedIcon = Icons.Filled.Favorite,
              unselectedIcon = Icons.Outlined.FavoriteBorder,
              hasNews = false),
          MenuNavigationItem(
              route = LocalContext.current.getString(R.string.socials),
              selectedIcon = Icons.Filled.Face,
              unselectedIcon = Icons.Outlined.Face,
              hasNews = true),
          MenuNavigationItem(
              route = LocalContext.current.getString(R.string.tab_profile),
              selectedIcon = Icons.Filled.Person,
              unselectedIcon = Icons.Outlined.Person,
              hasNews = false))

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination

  if (currentDestination?.route != LeafScreen.MoreInfo.route &&
      !(currentDestination?.route ?: "").contains(LeafScreen.Conversation.route)) {
    NavigationBar(modifier = Modifier.testTag("MenuNavigation")) {
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
  } else {
    Unit
  }
}
