package com.lastaoutdoor.lasta.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.screen.login.LoginScreen
import com.lastaoutdoor.lasta.ui.screen.setup.SetupScreen
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel

fun NavGraphBuilder.addLoginNavGraph(navController: NavHostController) {
  navigation(startDestination = DestinationRoute.SignIn.route, route = BaseRoute.Login.route) {
    composable(DestinationRoute.SignIn.route) { entry ->
      val authViewModel: AuthViewModel = entry.sharedViewModel(navController)
      val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)
      val isSignUp = authViewModel.isSignUp.collectAsState(initial = false).value
      LoginScreen(
          authViewModel.beginSignInResult,
          authViewModel.user,
          isSignUp,
          authViewModel::startGoogleSignIn,
          authViewModel::finishGoogleSignIn,
          authViewModel.oneTapClient,
          { user: UserModel ->
            preferencesViewModel.updateIsLoggedIn(true)
            preferencesViewModel.updateUserInfo(user)
          },
          {
            navController.navigate(DestinationRoute.Setup.route) {
              popUpTo(BaseRoute.Login.route) { inclusive = true }
            }
          },
          {
            navController.popBackStack()
            navController.navigate(BaseRoute.Main.route)
          },
        authViewModel)
    }
    composable(DestinationRoute.Setup.route) { entry ->
      val authViewModel: AuthViewModel = entry.sharedViewModel(navController)
      val preferencesViewModel: PreferencesViewModel = entry.sharedViewModel(navController)
      val userId = preferencesViewModel.userId.collectAsState(initial = "").value
      SetupScreen(
          userId = userId,
          updateFieldInUser = authViewModel::updateFieldInUser,
          updateLanguage = preferencesViewModel::updateLanguage,
          updatePrefActivity = preferencesViewModel::updatePrefActivity,
          navigateToMain = {
            navController.popBackStack()
            navController.navigate(BaseRoute.Main.route)
          })
    }
  }
}
