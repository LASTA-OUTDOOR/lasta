package com.lastaoutdoor.lasta.ui.screen.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.navigation.LeafScreen

@Composable
fun FavoritesScreen(
    navController: NavHostController,
) {

  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { navController.navigate(LeafScreen.MoreInfo.route) }) {
          Text(text = LocalContext.current.getString(R.string.more_info))
        }
      }
}
