package com.lastaoutdoor.lasta.ui.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.ui.navigation.MenuNavGraph
import com.lastaoutdoor.lasta.ui.navigation.MenuNavigation

@Composable
fun MainScreen(rootNavController: NavHostController) {
  val navController = rememberNavController()

  Scaffold(
      modifier = Modifier.testTag("MainScreen"),
      bottomBar = { MenuNavigation(navController = navController) }) { paddingValues ->
        MenuNavGraph(
            rootNavController = rootNavController,
            navController = navController,
            modifier = Modifier.padding(paddingValues))
      }
}
