package com.lastaoutdoor.lasta.ui.screen.discover

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.models.activity.Activity
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.ui.components.DisplaySelection
import com.lastaoutdoor.lasta.ui.components.DropDownMenuComponent
import com.lastaoutdoor.lasta.ui.components.LoadingAnim
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.ui.components.WeatherReportBig
import com.lastaoutdoor.lasta.ui.components.WeatherReportSmall
import com.lastaoutdoor.lasta.ui.components.searchBarComponent
import com.lastaoutdoor.lasta.ui.screen.map.mapScreen
import com.lastaoutdoor.lasta.utils.ConnectionState
import com.lastaoutdoor.lasta.utils.OrderingBy
import com.lastaoutdoor.lasta.viewmodel.DiscoverDisplayType
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenCallBacks
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DiscoverScreen(
    discoverScreenState: DiscoverScreenState,
    discoverScreenCallBacks: DiscoverScreenCallBacks,
    favorites: List<String>,
    flipFavorite: (String) -> Unit,
    navigateToFilter: () -> Unit,
    navigateToMoreInfo: () -> Unit,
    navigateToRangeSearch: () -> Unit,
    changeActivityToDisplay: (Activity) -> Unit,
    changeWeatherTarget: (Activity) -> Unit,
    weather: WeatherResponse?,
    isOnline: ConnectionState
) {

  var moveCamera: (CameraUpdate) -> Unit by remember { mutableStateOf({ _ -> }) }

  if (discoverScreenState.screen == DiscoverDisplayType.LIST) {
    Column(
        modifier =
            Modifier.testTag("discoveryScreen").background(MaterialTheme.colorScheme.background)) {
          HeaderComposable(
              discoverScreenState.screen,
              discoverScreenState.range,
              discoverScreenState.selectedLocality,
              discoverScreenCallBacks.fetchActivities,
              discoverScreenCallBacks.setScreen,
              { navigateToRangeSearch() },
              navigateToFilter,
              discoverScreenState.orderingBy,
              discoverScreenCallBacks.updateOrderingBy,
              weather,
              discoverScreenCallBacks.fetchSuggestion,
              discoverScreenState.suggestions,
              discoverScreenCallBacks.setSelectedLocality,
              discoverScreenCallBacks.clearSuggestions,
              discoverScreenCallBacks.updateInitialPosition,
              moveCamera,
              isOnline)

          if (discoverScreenState.isLoading) {
            if (isOnline == ConnectionState.CONNECTED) {
              LoadingAnim(width = 35, tag = "LoadingBarDiscover")
            } else {
              Column(
                  verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                    Text(
                        text = LocalContext.current.getString(R.string.offline_discover_text),
                        textAlign = TextAlign.Center)
                  }
            }
          } else if (discoverScreenState.activities.isEmpty()) {
            EmptyActivityList()
          } else {
            LazyColumn {
              item {
                Spacer(modifier = Modifier.height(8.dp))

                ActivitiesDisplay(
                    discoverScreenState.activities,
                    discoverScreenState.centerPoint,
                    favorites,
                    changeActivityToDisplay,
                    flipFavorite = flipFavorite,
                    navigateToMoreInfo = navigateToMoreInfo,
                    changeWeatherTarget = changeWeatherTarget,
                    isOnline = isOnline)
              }
            }
          }
        }
  } else if (discoverScreenState.screen == DiscoverDisplayType.MAP) {
    Column {
      HeaderComposable(
          discoverScreenState.screen,
          discoverScreenState.range,
          discoverScreenState.selectedLocality,
          discoverScreenCallBacks.fetchActivities,
          discoverScreenCallBacks.setScreen,
          navigateToRangeSearch,
          navigateToFilter,
          discoverScreenState.orderingBy,
          discoverScreenCallBacks.updateOrderingBy,
          weather,
          discoverScreenCallBacks.fetchSuggestion,
          discoverScreenState.suggestions,
          discoverScreenCallBacks.setSelectedLocality,
          discoverScreenCallBacks.clearSuggestions,
          discoverScreenCallBacks.updateInitialPosition,
          moveCamera,
          isOnline)
      Box(modifier = Modifier.fillMaxHeight().testTag("mapScreenDiscover")) {
        moveCamera =
            mapScreen(
                discoverScreenState.mapState,
                discoverScreenState.initialPosition,
                discoverScreenState.initialZoom,
                discoverScreenCallBacks.updateSelectedMarker,
                discoverScreenCallBacks.clearSelectedItinerary,
                discoverScreenState.selectedZoom,
                discoverScreenState.selectedMarker,
                discoverScreenState.selectedItinerary,
                discoverScreenState.markerList,
                discoverScreenCallBacks.clearSelectedMarker)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderComposable(
    screen: DiscoverDisplayType,
    range: Double,
    selectedLocality: Pair<String, LatLng>,
    fetchActivities: () -> Unit,
    setScreen: (DiscoverDisplayType) -> Unit,
    navigateToRangeSearch: () -> Unit,
    navigateToFilter: () -> Unit,
    orderingBy: OrderingBy,
    updateOrderingBy: (OrderingBy) -> Unit,
    weather: WeatherResponse?,
    fetchSuggestion: (String) -> Unit,
    suggestions: Map<String, LatLng>,
    setSelectedLocality: (Pair<String, LatLng>) -> Unit,
    clearSuggestions: () -> Unit,
    updateInitialPosition: (LatLng) -> Unit,
    moveCamera: (CameraUpdate) -> Unit,
    isOnline: ConnectionState
) {

  // Initialise the map, otherwise the icon functionality won't work
  MapsInitializer.initialize(LocalContext.current)

  // Dropdown menu boolean
  val displayWeather = remember { mutableStateOf(false) }
  if (displayWeather.value) {
    Dialog(onDismissRequest = { displayWeather.value = false }) {
      Surface { WeatherReportBig(weather = weather, displayWind = false) {} }
    }
  }

  Surface(
      modifier = Modifier.fillMaxWidth().testTag("header"),
      color = MaterialTheme.colorScheme.background) {
        Column {
          // Location bar
          Row(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(horizontal = 16.dp, vertical = 8.dp)
                      .testTag("locationBar"),
              verticalAlignment = Alignment.CenterVertically) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier.clickable(onClick = { navigateToRangeSearch() })
                            .testTag("locationRow")) {
                      Column {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = "Location icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp).testTag("localityIcon"))
                      }

                      Spacer(modifier = Modifier.fillMaxWidth(0.02f))

                      Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                          Text(
                              text = selectedLocality.first,
                              style = MaterialTheme.typography.bodyMedium,
                              modifier = Modifier.testTag("locationText"))

                          IconButton(
                              onClick = navigateToRangeSearch,
                              modifier = Modifier.size(24.dp).testTag("locationButton"),
                              enabled = (isOnline == ConnectionState.CONNECTED)) {
                                Icon(
                                    Icons.Outlined.KeyboardArrowDown,
                                    contentDescription = "Location button",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp).testTag("locationIcon"))
                              }
                        }

                        Text(
                            text =
                                "${LocalContext.current.getString(R.string.less_than)} ${(range / 1000).toInt()} km ${LocalContext.current.getString(R.string.around_you)}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.testTag("rangeText"))
                      }
                    }
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                  WeatherReportSmall(weather) { displayWeather.value = true }
                }
              }

          // Search bar with toggle buttons
          var changeText = { _: String -> }
          Row(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp)
                      .testTag("searchBar"),
              verticalAlignment = Alignment.CenterVertically) {
                changeText =
                    searchBarComponent(
                        Modifier.weight(1f).testTag("searchBarComponent"),
                        onSearch = { fetchSuggestion(it) })

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { navigateToFilter() },
                    modifier =
                        Modifier.size(56.dp)
                            .border(
                                1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .testTag("filterButton"),
                    enabled = isOnline == ConnectionState.CONNECTED) {
                      Icon(
                          painter = painterResource(id = R.drawable.filter_icon),
                          contentDescription = "Filter button",
                          modifier = Modifier.size(24.dp).testTag("filterIcon"))
                    }
              }

          val fManager = LocalFocusManager.current

          LazyColumn(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(horizontal = 16.dp, vertical = 8.dp)
                      .heightIn(0.dp, 130.dp)) {
                items(suggestions.count()) { i ->
                  val suggestion = suggestions.entries.elementAt(i)
                  val scope = rememberCoroutineScope()
                  Card(
                      modifier =
                          Modifier.fillMaxWidth().padding(4.dp).testTag("suggestion").clickable {
                            scope.launch {
                              fManager.clearFocus()
                              setSelectedLocality(Pair(suggestion.key, suggestion.value))
                              updateInitialPosition(suggestion.value)
                              moveCamera(CameraUpdateFactory.newLatLng(suggestion.value))
                              fetchActivities()
                              // add a delay because otherwise the focus suggestion are redisplayed
                              // if you were on the keyboard (probably due to changeText and
                              // clearFocus)
                              delay(300)
                              changeText(suggestion.key)
                              clearSuggestions()
                            }
                          }) {
                        Text(modifier = Modifier.padding(8.dp).height(20.dp), text = suggestion.key)
                      }
                }
              }
          Row(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 8.dp),
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
                    Modifier.fillMaxWidth()
                        .padding(16.dp, 8.dp, 16.dp, 8.dp)
                        .testTag("sortingText"),
                verticalAlignment = Alignment.CenterVertically) {
                  DropDownMenuComponent(
                      items = OrderingBy.values().toList(),
                      selectedItem = orderingBy,
                      onItemSelected = { o -> updateOrderingBy(o) },
                      toStr = { o -> o.resourcesToString(LocalContext.current) },
                      fieldText = LocalContext.current.getString(R.string.filter_by))
                }
          }
          HorizontalDivider()
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
    navigateToMoreInfo: () -> Unit,
    isOnline: ConnectionState
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
          val activityHasImage = a.activityImageUrl != ""
          Box(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
            if (activityHasImage) {
              AsyncImage(
                  model = a.activityImageUrl,
                  contentDescription = "activity image",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.matchParentSize())
            } else {
              Image(
                  painter = painterResource(id = R.drawable.default_activity_bg),
                  contentDescription = "activity image not found",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.matchParentSize().alpha(0.3f))
            }

            Column {
              Row(
                  modifier = Modifier.fillMaxWidth().padding(8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier =
                            Modifier.shadow(4.dp, RoundedCornerShape(30))
                                .background(
                                    color = a.difficulty.getColorByDifficulty(),
                                    RoundedCornerShape(10.dp))
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
                        modifier = Modifier.size(24.dp).testTag("${a.activityId}favoriteButton"),
                        enabled = isOnline == ConnectionState.CONNECTED) {
                          Icon(
                              imageVector =
                                  if (favorites.contains(a.activityId)) Icons.Filled.Favorite
                                  else Icons.Filled.FavoriteBorder,
                              contentDescription = "Favorite Button",
                              tint = MaterialTheme.colorScheme.primary,
                              modifier = Modifier.size(24.dp))
                        }
                  }

              Spacer(modifier = Modifier.height(8.dp))
              Row(
                  modifier = Modifier.fillMaxWidth().padding(8.dp),
                  verticalAlignment = Alignment.CenterVertically) {
                    if (activityHasImage) {
                      Text(
                          text = a.name,
                          color = Color.White,
                          fontWeight = FontWeight.Bold,
                          style =
                              MaterialTheme.typography.headlineMedium.copy(
                                  shadow =
                                      Shadow(color = Color.Black, offset = Offset(x = 1f, y = 2f))))
                    } else {
                      Text(
                          text = a.name,
                          color = MaterialTheme.colorScheme.onBackground,
                          fontWeight = FontWeight.Bold,
                          style = MaterialTheme.typography.headlineMedium)
                    }
                  }

              Spacer(modifier = Modifier.height(16.dp))
            }
          }

          Column {
            SeparatorComponent()
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                  Row {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${a.rating} (${a.numRatings})")
                  }

                  Text(text = "Difficulty: ${a.difficulty}")
                  Text(
                      text =
                          "${String.format("%.1f", SphericalUtil.computeDistanceBetween(centerPoint, LatLng(a.startPosition.lat, a.startPosition.lon)) / 1000)} km")
                }
          }
        }
  }
}

@Composable
fun EmptyActivityList() {
  Column(
      modifier = Modifier.fillMaxSize().testTag("EmptyActivityList").padding(18.dp, 25.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painterResource(id = R.drawable.not_found),
            contentDescription = "No Activities",
            modifier = Modifier.size(85.dp).testTag("NoActivitiesLogo"))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
          Text(
              text = LocalContext.current.getString(R.string.no_activities),
              style = MaterialTheme.typography.bodyLarge,
              textAlign = TextAlign.Center)
        }
      }
}
