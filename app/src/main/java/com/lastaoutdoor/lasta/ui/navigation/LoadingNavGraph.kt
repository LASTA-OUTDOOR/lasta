package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.lastaoutdoor.lasta.ui.screen.loading.LoadingScreen
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

fun NavGraphBuilder.addLoadingNavGraph(navController: NavHostController) {
  navigation(startDestination = DestinationRoute.Loading.route, route = BaseRoute.Loading.route) {
    composable(DestinationRoute.Loading.route,
        deepLinks = listOf(
            navDeepLink { uriPattern = "https://lasta.jerem.ch/activity/{activityId}" }
        ),
        arguments = listOf(
            navArgument("activityId") {
                type = NavType.StringType
            }
        )
    ) { entry ->


      val preferencesViewModel: PreferencesViewModel = hiltViewModel(entry)

      val activityId = entry.arguments?.getString("activityId") ?: ""

      LoadingScreen(
          isLoggedIn = preferencesViewModel.isLoggedIn.observeAsState(initial = null).value,
          navigateWhenLoggedIn = {

              //If the app is launched with a deep link, we navigate to the activity screen with the activityId and then we transmit it to the more info screen
              navController.navigate(BaseRoute.Main.route + if(activityId.isEmpty()) "/DEFAULT" else "/$activityId") {
                  popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
              }

          },
          navigateWhenLoggedOut = {
            navController.navigate(BaseRoute.Login.route) {
              popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
          })
    }
  }
}
