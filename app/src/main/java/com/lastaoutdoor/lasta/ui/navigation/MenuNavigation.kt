package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

data class MenuNavigationItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)

@Composable
fun MenuNavigation(navController: NavHostController) {
  /*
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

  if (currentDestination?.route != DestinationRoute.MoreInfo.route &&
      !(currentDestination?.route ?: "").contains(DestinationRoute.Conversation.route) &&
      currentDestination?.route != DestinationRoute.Settings.route) {
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
  }*/
}
