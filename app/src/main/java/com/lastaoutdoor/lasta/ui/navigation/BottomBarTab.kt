package com.lastaoutdoor.lasta.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.lastaoutdoor.lasta.R

enum class BottomBarTab(
    @StringRes val title: Int,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
) {
  DISCOVER(
      R.string.tab_discover,
      DestinationRoute.Discover.route,
      Icons.Filled.Search,
      Icons.Outlined.Search,
      false),
  FAVORITES(
      R.string.tab_favorites,
      DestinationRoute.Favorites.route,
      Icons.Filled.Favorite,
      Icons.Outlined.FavoriteBorder,
      false),
  SOCIALS(
      R.string.tab_socials,
      DestinationRoute.Socials.route,
      Icons.Filled.Face,
      Icons.Outlined.Face,
      true),
  PROFILE(
      R.string.tab_profile,
      DestinationRoute.Profile.route,
      Icons.Filled.Person,
      Icons.Outlined.Person,
      false)
}
