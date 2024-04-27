package com.lastaoutdoor.lasta.ui.navigation

open class Route(open val route: String)

sealed class BaseRoute(override val route: String) : Route(route) {

  object App : BaseRoute("App")

  object Root : BaseRoute("Root")

  object Loading : BaseRoute("Loading")

  object Login : BaseRoute("Login")

  object Main : BaseRoute("Main")
}

/** Sealed class representing all possible leaf screens in the application */
sealed class DestinationRoute(override val route: String) : Route(route) {

  object Loading : DestinationRoute("Loading")

  object SignIn : DestinationRoute("SignIn")

  object Setup : DestinationRoute("Setup")

  object Discover : DestinationRoute("Discover")

  object Favorites : DestinationRoute("Favorites")

  object Socials : DestinationRoute("Socials")

  object Profile : DestinationRoute("Profile")

  object MoreInfo : DestinationRoute("MoreInfo")

  object Filter : DestinationRoute("Filter")

  object Conversation : DestinationRoute("Conversation")

  object Notifications : DestinationRoute("Notifications")

  object FriendProfile : DestinationRoute("FriendProfile")

  object Settings : DestinationRoute("Settings")
}
