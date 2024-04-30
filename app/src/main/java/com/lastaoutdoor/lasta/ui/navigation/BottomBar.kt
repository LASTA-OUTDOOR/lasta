package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun BottomBar(tabs: Array<BottomBarTab>, currentRoute: String, navigateToRoute: (String) -> Unit) {
  NavigationBar {
    tabs.forEach { tab ->
      val selected = currentRoute == tab.route
      NavigationBarItem(
          selected = selected,
          onClick = { navigateToRoute(tab.route) },
          label = { Text(LocalContext.current.getString(tab.title)) },
          icon = {
            BadgedBox(badge = { if (tab.hasNews) Badge() }) {
              Icon(
                  imageVector =
                      if (selected) {
                        tab.selectedIcon
                      } else tab.unselectedIcon,
                  contentDescription = tab.route)
            }
          })
    }
  }
}
