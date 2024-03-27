package com.lastaoutdoor.lasta.view

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.view.screen.DiscoveryScreen
import com.lastaoutdoor.lasta.view.screen.MapScreen
import com.lastaoutdoor.lasta.view.screen.ProfileScreen

data class TopLevelDestination(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val textId: Int
)


val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination("map", R.drawable.map_icon, R.string.tab_map),
    TopLevelDestination("discover",R.drawable.discover_icon, R.string.tab_discover),
    TopLevelDestination("profile",R.drawable.profile_icon, R.string.tab_profile)
)
@Composable
fun MainMenu(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                TOP_LEVEL_DESTINATIONS.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(painter = painterResource(id = screen.icon),contentDescription = null, modifier = Modifier
                            .padding(1.dp)
                            .width(24.dp)
                            .height(24.dp),tint= Color.Black) },
                        label = { Text(stringResource(screen.textId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        modifier = Modifier
                            .width(412.dp)
                            .height(80.dp)
                            .background(
                                color = Color(0xFFEDEEF0),
                                shape = RoundedCornerShape(size = 0.dp)
                            )
                            .padding(start = 8.dp, end = 8.dp),
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "map", Modifier.padding(innerPadding)) {
            composable("map") { MapScreen() }
            composable("discover") { DiscoveryScreen() }
            composable("profile"){ ProfileScreen()}
        }
    }
}