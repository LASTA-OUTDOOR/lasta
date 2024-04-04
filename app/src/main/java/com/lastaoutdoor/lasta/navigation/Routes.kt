package com.lastaoutdoor.lasta.navigation

sealed class RootScreen(val route: String) {
  object Root : RootScreen("Root")

  object Loading : RootScreen("Loading")

  object Login : RootScreen("Login")

  object Main : RootScreen("Main")
}

sealed class LeafScreen(val route: String) {
  object Map : LeafScreen("Map")

  object Discover : LeafScreen("Discover")

  object Profile : LeafScreen("Profile")
}
