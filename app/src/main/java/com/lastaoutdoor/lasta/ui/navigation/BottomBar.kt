package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun BottomBar(tabs: Array<BottomBarTab>, currentRoute: String, navigateToRoute: (String) -> Unit) {
  NavigationBar {
    tabs.forEach { tab ->
      val selected = currentRoute == tab.route
      NavigationBarItem(
          selected = selected,
          onClick = { navigateToRoute(tab.route) },
          // if the language is German, use a smaller font size
          label = {
            Text(
                text = LocalContext.current.getString(tab.title),
                style =
                    TextStyle(
                        fontSize = if (Locale.getDefault().language == "de") 16.sp else 18.sp))
          },
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
