package com.lastaoutdoor.lasta.ui.screen.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.components.DisplaySelection
import com.lastaoutdoor.lasta.ui.components.SearchBarComponent
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.ui.navigation.LeafScreen
import com.lastaoutdoor.lasta.ui.screen.map.MapScreen
import com.lastaoutdoor.lasta.ui.theme.AccentGreen
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.viewmodel.DiscoveryScreenType
import com.lastaoutdoor.lasta.viewmodel.DiscoveryScreenViewModel

@Composable
fun DiscoveryScreen(
    navController: NavController,
    discoveryScreenViewModel: DiscoveryScreenViewModel = hiltViewModel()
) {
  val screen by discoveryScreenViewModel.screen.collectAsState()



    var isRangePopup by rememberSaveable { mutableStateOf(false) }

    RangeSearchComposable(
        discoveryScreenViewModel = discoveryScreenViewModel,
        isRangePopup = isRangePopup,
        onDismissRequest = { isRangePopup = false })


  if (screen == DiscoveryScreenType.LIST) {
    LazyColumn(
        modifier =
        Modifier
            .testTag("discoveryScreen")
            .background(MaterialTheme.colorScheme.background)) {
          item { HeaderComposable(updatePopup = {isRangePopup = true}) }

          item {
            SeparatorComponent() // Add a separator between the header and the activities
            Spacer(modifier = Modifier.height(8.dp))
            ActivitiesDisplay(navController)
          }
        }
  } else if (screen == DiscoveryScreenType.MAP) {
    MapScreen()
    HeaderComposable(updatePopup = {isRangePopup = true})
  }
}

@Composable
fun HeaderComposable(discoveryScreenViewModel: DiscoveryScreenViewModel = hiltViewModel(), updatePopup: () -> Unit) {
  val screen by discoveryScreenViewModel.screen.collectAsState()
    val range by discoveryScreenViewModel.range.collectAsState()
    val selectedLocality = discoveryScreenViewModel.selectedLocality.collectAsState().value
  val iconSize = 48.dp // Adjust icon size as needed

  Surface(
      modifier = Modifier
          .fillMaxWidth()
          .graphicsLayer { alpha = 0.9f },
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

                    IconButton(onClick = updatePopup, modifier = Modifier.size(24.dp)) {
                      Icon(
                          Icons.Outlined.KeyboardArrowDown,
                          contentDescription = "Filter",
                          modifier = Modifier.size(24.dp))
                    }
                  }

                  Text(text = "Less than ${(range / 1000).toInt()} km", style = MaterialTheme.typography.bodySmall)
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
                IconButton(onClick = { /*TODO*/}, modifier = Modifier.size(iconSize)) {
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
                DisplaySelection(
                    DiscoveryScreenType.values().toList(),
                    screen,
                    discoveryScreenViewModel::setScreen,
                    DiscoveryScreenType::toString)
              }

          if (screen == DiscoveryScreenType.LIST) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                  Text("Filtering by:", style = MaterialTheme.typography.bodyMedium)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                      text = "Relevance",
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.primary)

                  IconButton(onClick = { /*TODO*/
                  }, modifier = Modifier.size(24.dp)) {
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
    navController: NavController,
    discoveryScreenViewModel: DiscoveryScreenViewModel = hiltViewModel()
) {
  val activities = discoveryScreenViewModel.climbingActivities
  for (a in activities) {
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = { navController.navigate(LeafScreen.MoreInfo.route) }),
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
                        text = "Climbing",
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
                  text = a.locationName ?: "Unnamed Activity",
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
              Text(text = "? popularity")
              Spacer(modifier = Modifier.width(8.dp))
              Text(text = if (a.difficulty != 0) "Difficulty: ${a.difficulty}" else "? difficulty")
              Spacer(modifier = Modifier.width(16.dp))
              // Distance from the user's location, NOT THE LENGTH OF THE ACTIVITY!!!
              Text(text = "? km")
            }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSearchComposable(discoveryScreenViewModel: DiscoveryScreenViewModel = hiltViewModel(), isRangePopup: Boolean, onDismissRequest: () -> Unit){
    //create local variable to hold the current range whcih will then be sent as argument to the discoveryScreenViewModel.getActivities with the range
    val range by discoveryScreenViewModel.range.collectAsState()
    val screen by discoveryScreenViewModel.screen.collectAsState()


    //list view search popup
    if (isRangePopup && screen == DiscoveryScreenType.LIST) {

        ModalBottomSheet(onDismissRequest = onDismissRequest, modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                //Select the City
                Text(
                    text = "Select the locality",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(500)
                    ))
                Spacer(modifier = Modifier.height(8.dp))
                //Dropdown to select the city
                CitySelectionDropdown(discoveryScreenViewModel)
                //Select the distance radius
                Text(
                    text = "Select the distance radius",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(500)
                    ))
                Spacer(modifier = Modifier.height(8.dp))
                //Slider to select the range
                Row {
                    Slider(
                        value = range.toFloat(),
                        onValueChange = {  discoveryScreenViewModel.setRange(it.toDouble().coerceIn(1000.0, 50000.0)) },
                        valueRange = 0f..50000f,
                        steps = 100,
                        modifier = Modifier.width(305.dp)
                    )
                    Text(
                        //put range in km
                        text = "${(range / 1000).toInt()} km",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                //Button to apply the range
                ElevatedButton(
                    onClick = {
                        discoveryScreenViewModel.fetchClimbingActivities(range, discoveryScreenViewModel.selectedLocality.value.second)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .width(305.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text(
                        "Search",
                        style =
                        TextStyle(
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight(400),
                        )
                    )
                }
            }
        }
    }

    //map range search popup
    if (isRangePopup && screen == DiscoveryScreenType.MAP) {
        ModalBottomSheet(onDismissRequest = onDismissRequest, modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select the distance",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(500)
                ))
                Spacer(modifier = Modifier.height(8.dp))
                //Slider to select the range
                Row {
                    Slider(
                        value = range.toFloat(),
                        onValueChange = { discoveryScreenViewModel.setRange(it.toDouble().coerceIn(1000.0, 50000.0)) },
                        valueRange = 0f..50000f,
                        steps = 100,
                        modifier = Modifier.width(305.dp)
                    )
                    Text(
                        //put range in km
                        text = "${(range / 1000).toInt()} km",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                //Button to apply the range
                ElevatedButton(
                    onClick = {
                        //discoveryScreenViewModel.getActivities(range)
                        onDismissRequest()
                    },
                    modifier = Modifier
                        .width(305.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text(
                        "Apply",
                        style =
                        TextStyle(
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            fontWeight = FontWeight(400),
                        )
                    )
                }
            }
        }
    }
}

//Dropdown to select the city
@Composable
fun CitySelectionDropdown(discoveryScreenViewModel: DiscoveryScreenViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val localities = discoveryScreenViewModel.localities
    var selectedLocality = discoveryScreenViewModel.selectedLocality.collectAsState().value

    Box(modifier = Modifier.wrapContentSize()) {
        Text(selectedLocality.first, modifier = Modifier.clickable(onClick = { expanded = true }), style = TextStyle(
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight(400),
            color = AccentGreen
        ))
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            localities.forEach { locality ->
                DropdownMenuItem(
                    text = { Text(locality.first, color = AccentGreen) },
                    onClick = {
                        discoveryScreenViewModel.setSelectedLocality(locality)
                        expanded = false
                })
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}