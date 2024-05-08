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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.map.MapItinerary
import com.lastaoutdoor.lasta.models.map.Marker
import com.lastaoutdoor.lasta.ui.components.DisplaySelection
import com.lastaoutdoor.lasta.ui.components.LoadingAnim
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.ui.components.WeatherReportBig
import com.lastaoutdoor.lasta.ui.components.WeatherReportSmall
import com.lastaoutdoor.lasta.ui.components.searchBarComponent
import com.lastaoutdoor.lasta.ui.screen.discover.components.ModalUpperSheet
import com.lastaoutdoor.lasta.ui.screen.discover.components.RangeSearchComposable
import com.lastaoutdoor.lasta.ui.screen.map.MapScreen
import com.lastaoutdoor.lasta.utils.OrderingBy
import com.lastaoutdoor.lasta.viewmodel.DiscoverDisplayType
import com.lastaoutdoor.lasta.viewmodel.MapState

@Composable
fun DiscoverScreen(
    isLoading: Boolean,
    activities: List<Activity>,
    screen: DiscoverDisplayType,
    range: Double,
    centerPoint: LatLng,
    favorites: List<String>,
    localities: List<Pair<String, LatLng>>,
    selectedLocality: Pair<String, LatLng>,
    fetchActivities: (Double, LatLng) -> Unit,
    setScreen: (DiscoverDisplayType) -> Unit,
    setRange: (Double) -> Unit,
    setSelectedLocality: (Pair<String, LatLng>) -> Unit,
    flipFavorite: (String) -> Unit,
    navigateToFilter: () -> Unit,
    navigateToMoreInfo: () -> Unit,
    changeActivityToDisplay: (Activity) -> Unit,
    changeWeatherTarget: (Activity) -> Unit,
    weather: WeatherResponse?,
    state: MapState,
    updatePermission: (Boolean) -> Unit,
    initialPosition: LatLng,
    initialZoom: Float,
    updateMarkers: (LatLng, Double) -> Unit,
    updateSelectedMarker: (Marker?) -> Unit,
    clearSelectedItinerary: () -> Unit,
    selectedZoom: Float,
    selectedMarker: Marker?,
    selectedItinerary: MapItinerary?,
    markerList: List<Marker>,
    orderingBy: OrderingBy,
    updateOrderingBy: (OrderingBy) -> Unit,
    clearSelectedMarker: () -> Unit,
    fetchSuggestion: (String) -> Unit,
    suggestions: Map<String, LatLng>,
    clearSuggestions: () -> Unit
) {

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
    Column(
        modifier =
        Modifier
            .testTag("discoveryScreen")
            .background(MaterialTheme.colorScheme.background)) {
          HeaderComposable(
              screen,
              range,
              selectedLocality,
              setScreen,
              { isRangePopup = true },
              navigateToFilter,
              orderingBy,
              updateOrderingBy,
              weather,
              fetchSuggestion,
              suggestions,
              setSelectedLocality,
              fetchActivities,
              clearSuggestions)

          Spacer(modifier = Modifier.height(8.dp))
          if (isLoading) {
            LoadingAnim(width = 35, tag = "LoadingBarDiscover")
          } else if (activities.isEmpty()) {
            /* TODO */
          } else {
            LazyColumn {
              item {
                ActivitiesDisplay(
                    activities,
                    centerPoint,
                    favorites,
                    changeActivityToDisplay,
                    flipFavorite = flipFavorite,
                    navigateToMoreInfo = navigateToMoreInfo,
                    changeWeatherTarget = changeWeatherTarget)
              }
            }
          }
        }
  } else if (screen == DiscoverDisplayType.MAP) {
    Column {
      HeaderComposable(
          screen,
          range,
          selectedLocality,
          setScreen,
          { isRangePopup = true },
          navigateToFilter,
          orderingBy,
          updateOrderingBy,
          weather,
          fetchSuggestion,
          suggestions,
          setSelectedLocality,
          fetchActivities,
          clearSuggestions)
      Box(modifier = Modifier
          .fillMaxHeight()
          .testTag("mapScreenDiscover")) {
        MapScreen(
            state,
            updatePermission,
            initialPosition,
            initialZoom,
            updateMarkers,
            updateSelectedMarker,
            clearSelectedItinerary,
            selectedZoom,
            selectedMarker,
            selectedItinerary,
            markerList,
            clearSelectedMarker)
      }
    }
  }

  // Add the modal upper sheet
  ModalUpperSheet(isRangePopup = isRangePopup)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderComposable(
    screen: DiscoverDisplayType,
    range: Double,
    selectedLocality: Pair<String, LatLng>,
    setScreen: (DiscoverDisplayType) -> Unit,
    updatePopup: () -> Unit,
    navigateToFilter: () -> Unit,
    orderingBy: OrderingBy,
    updateOrderingBy: (OrderingBy) -> Unit,
    weather: WeatherResponse?,
    fetchSuggestion: (String) -> Unit,
    suggestions: Map<String, LatLng>,
    setSelectedLocality: (Pair<String, LatLng>) -> Unit,
    fetchActivities: (Double, LatLng) -> Unit,
    clearSuggestions: () -> Unit
) {
  // Dropdown menu boolean
  var showMenu by remember { mutableStateOf(false) }

  val iconSize = 48.dp // Adjust icon size as needed
  val displayWeather = remember { mutableStateOf(false) }
  if (displayWeather.value) {
    Dialog(onDismissRequest = { displayWeather.value = false }) {
      Surface { WeatherReportBig(weather = weather, displayWind = false) }
    }
  }
  Surface(
      modifier = Modifier
          .fillMaxWidth()
          .testTag("header"),
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
                    Text(
                        text = selectedLocality.first,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.testTag("locationText"))

                    IconButton(
                        onClick = updatePopup,
                        modifier = Modifier
                            .size(24.dp)
                            .testTag("locationButton")) {
                          Icon(
                              Icons.Outlined.KeyboardArrowDown,
                              contentDescription = "Location button",
                              modifier = Modifier
                                  .size(24.dp)
                                  .testTag("locationIcon"))
                        }
                  }

                  Text(
                      text =
                          "${LocalContext.current.getString(R.string.less_than)} ${(range / 1000).toInt()} km ${LocalContext.current.getString(R.string.around_you)}",
                      style = MaterialTheme.typography.bodySmall,
                      modifier = Modifier.testTag("rangeText"))
                }
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                  WeatherReportSmall(weather) { displayWeather.value = true }
                }
              }

          // Search bar with toggle buttons
          var changeText = {_:String ->}
          Row(
              modifier =
              Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 8.dp)
                  .testTag("searchBar"),
              verticalAlignment = Alignment.CenterVertically) {
                changeText = searchBarComponent(
                        Modifier
                            .weight(1f)
                            .testTag("searchBarComponent"),
                        onSearch = { fetchSuggestion(it) })

              Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                    onClick = { navigateToFilter() },
                    modifier = Modifier
                        .size(iconSize)
                        .testTag("filterButton")) {
                      Icon(
                          painter = painterResource(id = R.drawable.filter_icon),
                          contentDescription = "Filter button",
                          modifier = Modifier
                              .size(24.dp)
                              .testTag("filterIcon"))
                    }
              }

          val fManager = LocalFocusManager.current
          // Suggestions for the places

          LazyColumn(modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .heightIn(0.dp, 130.dp)) {
              items(suggestions.count()){i ->
                val suggestion = suggestions.entries.elementAt(i)
                  Card(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .testTag("suggestion")
                        .clickable {
                            fManager.clearFocus()
                            setSelectedLocality(Pair(suggestion.key, suggestion.value))
                            fetchActivities(range, suggestion.value)
                            changeText(suggestion.key)
                            clearSuggestions()
                        }) {
                      Text(modifier = Modifier.padding(8.dp).height(20.dp), text = suggestion.key)
                }
            }
          }
          Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center) {
                val context = LocalContext.current
                DisplaySelection(DiscoverDisplayType.values().toList(), screen, setScreen) {
                  it.toStringCon(context)
                }
              }

          if (screen == DiscoverDisplayType.LIST) {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("sortingText"),
                verticalAlignment = Alignment.CenterVertically) {
                  Text(
                      LocalContext.current.getString(R.string.filter_by),
                      style = MaterialTheme.typography.bodyMedium)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                      text = LocalContext.current.getString(orderingBy.orderText),
                      modifier = Modifier.testTag("sortingTextValue"),
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.primary)

                  IconButton(
                      onClick = { showMenu = showMenu.not() },
                      modifier = Modifier
                          .size(24.dp)
                          .testTag("sortingButton")) {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Ordering button",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .testTag("sortingIcon"))
                      }
                }
          }
        }
      }

  if (showMenu) {
    OrderingBy.values().forEach { order ->
      DropdownMenuItem(
          text = { Text(text = LocalContext.current.getString(order.orderText)) },
          onClick = {
            if (order != orderingBy) {
              updateOrderingBy(order)
            }
            showMenu = false
          },
          modifier = Modifier.testTag("sortingItem" + order.name))
    }
  }
}

@Composable
fun ActivitiesDisplay(
    activities: List<Activity>,
    centerPoint: LatLng,
    favorites: List<String>,
    changeActivityToDisplay: (Activity) -> Unit,
    changeWeatherTarget: (Activity) -> Unit,
    flipFavorite: (String) -> Unit,
    navigateToMoreInfo: () -> Unit
) {

  for (a in activities) {
    Card(
        modifier =
            Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .clickable(
                    onClick = {
                      changeActivityToDisplay(a)
                      changeWeatherTarget(a)
                      navigateToMoreInfo()
                    })
                .testTag("${a.activityId}activityCard"),
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
                  onClick = { flipFavorite(a.activityId) },
                  modifier = Modifier
                      .size(24.dp)
                      .testTag("${a.activityId}favoriteButton")) {
                    Icon(
                        imageVector =
                            if (favorites.contains(a.activityId)) Icons.Filled.Favorite
                            else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite Button",
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
              Text(text = "${a.rating} (${a.numRatings})")
              Spacer(modifier = Modifier.width(8.dp))
              Text(text = "Difficulty: ${a.difficulty}")
              Spacer(modifier = Modifier.width(16.dp))
              Text(
                  text =
                      "${String.format("%.1f", SphericalUtil.computeDistanceBetween(centerPoint, LatLng(a.startPosition.lat, a.startPosition.lon)) / 1000)} km")
            }
      }
    }
  }
}
