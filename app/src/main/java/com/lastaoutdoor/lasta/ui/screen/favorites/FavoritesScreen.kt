package com.lastaoutdoor.lasta.ui.screen.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.ui.components.LoadingAnim
import com.lastaoutdoor.lasta.ui.screen.discover.ActivitiesDisplay

@Composable
fun FavoritesScreen(
    isLoading: Boolean,
    activities: List<Activity>,
    centerPoint: LatLng,
    favorites: List<String>,
    changeActivityToDisplay: (Activity) -> Unit,
    changeWeatherTarget: (Activity) -> Unit,
    flipFavorite: (String) -> Unit,
    updateFavorites: () -> Unit,
    navigateToMoreInfo: () -> Unit,
) {
  LaunchedEffect(Unit) {
    // update so that when going back from more info screen with having modified activity, the
    // screen is updated
    updateFavorites()
  }

  Column(modifier = Modifier.testTag("FavoritesScreen")) {
    Box(
        modifier = Modifier.fillMaxWidth().height(70.dp),
        contentAlignment = Alignment.CenterStart) {
          Text(
              text = LocalContext.current.getString(R.string.tab_favorites),
              style = MaterialTheme.typography.headlineLarge,
              modifier = Modifier.padding(start = 20.dp))
        }
    HorizontalDivider()
    if (isLoading) {
      LoadingAnim(width = 35, tag = "LoadingBarFavorites")
    } else if (favorites.isEmpty() || favorites[0].isEmpty()) {
      // the second condition is to avoid being baited by a potential empty favorite activity
      EmptyFavoritesList()
    } else {
      LazyColumn {
        item {
          Spacer(modifier = Modifier.height(8.dp))
          ActivitiesDisplay(
              activities,
              centerPoint,
              favorites,
              changeActivityToDisplay,
              changeWeatherTarget,
              flipFavorite,
              navigateToMoreInfo)
        }
      }
    }
  }
}

@Composable
fun EmptyFavoritesList() {
  Column(
      modifier = Modifier.fillMaxSize().testTag("EmptyFavoritesList"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painterResource(id = R.drawable.no_fav_logo),
            contentDescription = "No Favorites",
            modifier = Modifier.size(50.dp).testTag("NoFavLogo"))
        Text(
            text = LocalContext.current.getString(R.string.no_fav),
            style =
                TextStyle(
                    fontWeight = FontWeight.Normal, fontSize = 20.sp, textAlign = TextAlign.Center),
            modifier = Modifier.padding(15.dp))
      }
}
