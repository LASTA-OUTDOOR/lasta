package com.lastaoutdoor.lasta.ui.screen.favorites

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.ui.screen.discover.ActivitiesDisplay

@Composable
fun FavoritesScreen(
    activities: List<Activity>,
    centerPoint: LatLng,
    favorites: List<String>,
    changeActivityToDisplay: (Activity) -> Unit,
    flipFavorite: (String) -> Unit,
    navigateToMoreInfo: () -> Unit
) {
  LazyColumn {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      ActivitiesDisplay(
          activities,
          centerPoint,
          favorites,
          changeActivityToDisplay,
          flipFavorite,
          navigateToMoreInfo)
    }
  }
}
