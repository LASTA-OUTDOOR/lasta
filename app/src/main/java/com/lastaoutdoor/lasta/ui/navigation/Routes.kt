package com.lastaoutdoor.lasta.ui.navigation

/** Sealed class representing all possible root screens in the application */
sealed class RootScreen(val route: String) {
  object Root : RootScreen("Root")

  object Loading : RootScreen("Loading")

  object Login : RootScreen("Login")

  object Main : RootScreen("Main")
}

/** Sealed class representing all possible leaf screens in the application */
sealed class LeafScreen(val route: String) {
  object Social : LeafScreen("Community")

  object Discover : LeafScreen("Discover")

  object Profile : LeafScreen("Profile")
}
