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

  object Discover : LeafScreen("Discover")

  object Discover2 : LeafScreen("Découvrir")

  object Discover3 : LeafScreen("Entdecken")

  object Favorites : LeafScreen("Favorites")

  object Favorites2 : LeafScreen("Favoriten")

  object Favorites3 : LeafScreen("Favoris")

  object Social : LeafScreen("Socials")

  object Social2 : LeafScreen("Amis")

  object Social3 : LeafScreen("Freunde")

  object Profile : LeafScreen("Profile")

  object Profile2 : LeafScreen("Profil")

  object SignIn : LeafScreen("SignIn")

  object Setup : LeafScreen("Setup")

  object MoreInfo : LeafScreen("MoreInfo")

  object Conversation : LeafScreen("Conversation")

  object Notifications : LeafScreen("Notifications")

  object FriendProfile : LeafScreen("FriendProfile")
}
