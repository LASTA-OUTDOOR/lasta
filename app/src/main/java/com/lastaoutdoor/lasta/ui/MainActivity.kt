package com.lastaoutdoor.lasta.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.navigation.RootScreen
import com.lastaoutdoor.lasta.ui.screen.LoginScreen
import com.lastaoutdoor.lasta.ui.screen.MainScreen
import com.lastaoutdoor.lasta.ui.theme.LastaTheme
import com.lastaoutdoor.lasta.viewmodel.AuthViewModel
import com.lastaoutdoor.lasta.viewmodel.MapViewModel
import com.lastaoutdoor.lasta.viewmodel.OutdoorActivityViewModel
import com.lastaoutdoor.lasta.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val authViewModel by viewModels<AuthViewModel>()
  private val preferencesViewModel by viewModels<PreferencesViewModel>()
  private val outdoorActivityViewModel by viewModels<OutdoorActivityViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      // The navcontroller that allows to go from the login screen to the main menu screen
      val navController = rememberNavController()
      val authState by authViewModel.authStateFlow.collectAsState()
      val isLoggedIn by preferencesViewModel.isLoggedIn.collectAsState(initial = false)

      // TODO: Make different composables to reduce this file size
      LastaTheme {
        NavHost(
            navController = navController,
            route = RootScreen.Root.route,
            startDestination = RootScreen.Loading.route) {
              composable(RootScreen.Loading.route) {
                LaunchedEffect(key1 = isLoggedIn) {
                  delay(400)
                  navController.popBackStack()
                  if (isLoggedIn) {
                    navController.navigate(RootScreen.Main.route)
                  } else {
                    navController.navigate(RootScreen.Login.route)
                  }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                  CircularProgressIndicator(modifier = Modifier.width(100.dp))
                }
              }
              composable(RootScreen.Login.route) {
                LoginScreen(
                    authViewModel,
                    onLogin = {
                      preferencesViewModel.updateIsLoggedIn(true)
                      val user = authViewModel.getCurrentUser()
                      preferencesViewModel.updateUserInfo(
                          user?.userId ?: "",
                          user?.userName ?: "",
                          user?.email ?: "",
                          user?.profilePictureUrl.toString())
                      navController.popBackStack()
                      navController.navigate(RootScreen.Main.route)
                    })
              }
              composable(RootScreen.Main.route) {
                LaunchedEffect(key1 = Unit) { authViewModel.fetchAuthInfo() }
                LaunchedEffect(key1 = authState) {
                  if (authViewModel.getCurrentUser() == null) {
                    preferencesViewModel.updateIsLoggedIn(false)
                    navController.popBackStack()
                    navController.navigate(RootScreen.Login.route)
                  } else {
                    preferencesViewModel.updateIsLoggedIn(true)
                    val user = authViewModel.getCurrentUser()
                    preferencesViewModel.updateUserInfo(
                        user?.userId ?: "",
                        user?.userName ?: "",
                        user?.email ?: "",
                        user?.profilePictureUrl.toString())
                  }
                }
                MainScreen(
                    preferencesViewModel = preferencesViewModel,
                    outdoorActivityViewModel = outdoorActivityViewModel,
                    onSignOut = {
                      authViewModel.signOut()
                      preferencesViewModel.updateIsLoggedIn(false)
                      navController.popBackStack()
                      navController.navigate(RootScreen.Login.route)
                    })
              }
            }
      }
    }
  }
}
