package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.ui.components.DisplaySelection
import com.lastaoutdoor.lasta.ui.components.SearchBarComponent
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.ui.components.WeatherReportBig
import com.lastaoutdoor.lasta.ui.components.WeatherReportSmall
import com.lastaoutdoor.lasta.ui.screen.discover.components.ModalUpperSheet
import com.lastaoutdoor.lasta.ui.screen.discover.components.RangeSearchComposable
import com.lastaoutdoor.lasta.ui.screen.map.MapScreen
import com.lastaoutdoor.lasta.viewmodel.DiscoverDisplayType
import com.lastaoutdoor.lasta.viewmodel.MapState

@Composable
fun DiscoverScreen(
    activities: List<Activity>,
    screen: DiscoverDisplayType,
    range: Double,
    localities: List<Pair<String, LatLng>>,
    selectedLocality: Pair<String, LatLng>,
    fetchActivities: (Double, LatLng) -> Unit,
    setScreen: (DiscoverDisplayType) -> Unit,
    setRange: (Double) -> Unit,
    setSelectedLocality: (Pair<String, LatLng>) -> Unit,
    navigateToFilter: () -> Unit,
    navigateToMoreInfo: () -> Unit,
    changeActivityToDisplay: (Activity) -> Unit,
    weather: WeatherResponse?,
    state: MapState,
    updatePermission: (Boolean) -> Unit,
    initialPosition: LatLng,
    initialZoom: Float,
    updateMarkers: (LatLng, Double) -> Unit,
    updateSelectedMarker: (Marker) -> Unit,
    clearSelectedItinerary: () -> Unit,
    selectedZoom: Float,
    updateSelectedItinerary: (Long) -> Unit) {

  var isRangePopup by rememberSaveable { mutableStateOf(false) }

  RangeSearchComposable(
      screen,
      range,
      localities,
      selectedLocality,
      setRange,
      setSelectedLocality,
      fetchActivities,
      isRangePopup) {
        isRangePopup = false
      }

  if (screen == DiscoverDisplayType.LIST) {
    LazyColumn(
        modifier =
        Modifier
            .testTag("discoveryScreen")
            .background(MaterialTheme.colorScheme.background)) {
          item {
            HeaderComposable(
                screen,
                range,
                selectedLocality,
                setScreen,
                { isRangePopup = true },
                navigateToFilter,
                weather)
          }

          item {
            Spacer(modifier = Modifier.height(8.dp))
            ActivitiesDisplay(activities, changeActivityToDisplay, navigateToMoreInfo)
          }
        }
  } else if (screen == DiscoverDisplayType.MAP) {
    Column {
      HeaderComposable(

          screen, range, selectedLocality, setScreen, { isRangePopup = true }, navigateToFilter,weather)
      Box(modifier = Modifier.fillMaxHeight()) {
        MapScreen(
            state,
            updatePermission,
            initialPosition,
            initialZoom,
            updateMarkers,
            updateSelectedMarker,
            clearSelectedItinerary,
            selectedZoom,
            updateSelectedItinerary)
      }
    }
  }

  // Add the modal upper sheet
  ModalUpperSheet(isRangePopup = isRangePopup)
}

@Composable
fun HeaderComposable(
    screen: DiscoverDisplayType,
    range: Double,
    selectedLocality: Pair<String, LatLng>,
    setScreen: (DiscoverDisplayType) -> Unit,
    updatePopup: () -> Unit,
    navigateToFilter: () -> Unit,
    weather: WeatherResponse?
) {
  val iconSize = 48.dp // Adjust icon size as needed
  val displayWeather= remember{mutableStateOf(false)}
  if(displayWeather.value){
      Dialog(onDismissRequest = {displayWeather.value=false}) {
          Surface { WeatherReportBig(weather = weather,displayWind=false) }
      }
  }
  Surface(
      modifier = Modifier.fillMaxWidth(),
      color = MaterialTheme.colorScheme.background,
      shadowElevation = 3.dp) {
        Column {
          // Location bar
          Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically) {
                Column {
                  Row {
                    Text(text = selectedLocality.first, style = MaterialTheme.typography.bodyMedium)

                    IconButton(
                        onClick = updatePopup,
                        modifier = Modifier
                            .size(24.dp)
                            .testTag("listSearchOptionsEnableButton")) {
                          Icon(
                              Icons.Outlined.KeyboardArrowDown,
                              contentDescription = "Filter",
                              modifier = Modifier.size(24.dp))
                        }
                  }

                  Text(
                      text =
                          "${LocalContext.current.getString(R.string.less_than)} ${(range / 1000).toInt()} km ${LocalContext.current.getString(R.string.around_you)}",
                      style = MaterialTheme.typography.bodySmall)
                }
                  Column(modifier = Modifier
                      .fillMaxWidth(), horizontalAlignment = Alignment.End) {
                      WeatherReportSmall(weather) {displayWeather.value=true}
                  }
              }

          // Search bar with toggle buttons
          Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically) {
                SearchBarComponent(Modifier.weight(1f), onSearch = { /*TODO*/})
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { navigateToFilter() }, modifier = Modifier.size(iconSize)) {
                  Icon(
                      painter = painterResource(id = R.drawable.filter_icon),
                      contentDescription = "Filter",
                      modifier = Modifier.size(24.dp))
                }
              }
          Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center) {
                val contex = LocalContext.current
                DisplaySelection(DiscoverDisplayType.values().toList(), screen, setScreen) {
                  it.toStringCon(contex)
                }
              }

          if (screen == DiscoverDisplayType.LIST) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                  Text(
                      LocalContext.current.getString(R.string.filter_by),
                      style = MaterialTheme.typography.bodyMedium)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                      text = LocalContext.current.getString(R.string.relevance),
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.primary)

                  IconButton(onClick = { /*TODO*/}, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp))
                  }
                }
          }
        }
      }
}

@Composable
fun ActivitiesDisplay(
    activities: List<Activity>,
    changeActivityToDisplay: (Activity) -> Unit,
    navigateToMoreInfo: () -> Unit
) {
  for (a in activities) {
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(
                onClick = {
                    changeActivityToDisplay(a)
                    navigateToMoreInfo()
                }),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
      Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
              Box(
                  modifier =
                  Modifier
                      .shadow(4.dp, RoundedCornerShape(30))
                      .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                      .padding(PaddingValues(8.dp))) {
                    Text(
                        text =
                            LocalContext.current.getString(
                                when (a.activityType) {
                                  ActivityType.HIKING -> R.string.hiking
                                  ActivityType.CLIMBING -> R.string.climbing
                                  ActivityType.BIKING -> R.string.biking
                                }),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary)
                  }

              IconButton(
                  onClick = { /*TODO*/ /* changed to this when favorite Icons.Filled.Favorite*/},
                  modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = "Filter",
                        modifier = Modifier.size(24.dp))
                  }
            }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = a.name,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold)
            }
        SeparatorComponent()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
              Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = "Rating",
                  tint = MaterialTheme.colorScheme.primary)
              Text(text = "? ${LocalContext.current.getString(R.string.popularity)}")
              Spacer(modifier = Modifier.width(8.dp))
              Text(text = "Difficulty: ${LocalContext.current.getString(R.string.difficulty)}")
              Spacer(modifier = Modifier.width(16.dp))
              // Distance from the user's location, NOT THE LENGTH OF THE ACTIVITY!!!
              Text(text = "? km")
            }
      }
    }
  }
}
