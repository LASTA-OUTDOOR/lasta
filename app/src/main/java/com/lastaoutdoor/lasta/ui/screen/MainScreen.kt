package com.lastaoutdoor.lasta.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.navigation.MenuNavGraph
import com.lastaoutdoor.lasta.navigation.MenuNavigation

@Composable
fun MainScreen(onSignOut: () -> Unit) {
  val navController = rememberNavController()
  Scaffold(bottomBar = { MenuNavigation(navController = navController) }) { paddingValues ->
    MenuNavGraph(
        navController = navController,
        modifier = Modifier.padding(paddingValues),
        onSignOut = { onSignOut() })
  }
}
