package com.lastaoutdoor.lasta.ui.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.navigation.MenuNavGraph
import com.lastaoutdoor.lasta.ui.navigation.MenuNavigation
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.viewmodel.ConnectivityViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    connectivityViewModel: ConnectivityViewModel = hiltViewModel()
) {
  val navController = rememberNavController()
  val snackBarHostState = remember { SnackbarHostState() }
  val connectionState = connectivityViewModel.connectionState.collectAsState()
  val offlineString = LocalContext.current.getString(R.string.offline)

  if (connectionState.value == ConnectionState.OFFLINE) {
    LaunchedEffect(connectionState) {
      snackBarHostState.showSnackbar(
          message = offlineString, withDismissAction = true, duration = SnackbarDuration.Indefinite)
    }
  }

  Scaffold(
      modifier = Modifier.testTag("MainScreen"),
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
      bottomBar = { MenuNavigation(navController = navController) }) { paddingValues ->
        MenuNavGraph(
            rootNavController = rootNavController,
            navController = navController,
            modifier = Modifier.padding(paddingValues))
      }
}
