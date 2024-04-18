package com.lastaoutdoor.lasta.ui.screen.discovery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.components.DisplaySelection
import com.lastaoutdoor.lasta.ui.components.SearchBarComponent
import com.lastaoutdoor.lasta.ui.screen.map.MapScreen
import com.lastaoutdoor.lasta.viewmodel.DiscoveryScreenType
import com.lastaoutdoor.lasta.viewmodel.DiscoveryScreenViewModel

@Composable
fun DiscoveryScreen(discoveryScreenViewModel: DiscoveryScreenViewModel = hiltViewModel()) {
  val screen by discoveryScreenViewModel.screen.collectAsState()

  if (screen == DiscoveryScreenType.LIST) {
    LazyColumn(modifier = Modifier.testTag("discoveryScreen")) {
      item { HeaderComposable() }

      item {
        Spacer(modifier = Modifier.height(8.dp))
        ActivitiesDisplay()
      }
    }
  } else if (screen == DiscoveryScreenType.MAP) {
    Column {
      HeaderComposable()
      Box(modifier = Modifier.fillMaxHeight()) { MapScreen() }
    }
  }
}

@Preview
@Composable
fun HeaderComposable(discoveryScreenViewModel: DiscoveryScreenViewModel = hiltViewModel()) {
  val screen by discoveryScreenViewModel.screen.collectAsState()
  val iconSize = 48.dp // Adjust icon size as needed

  Surface(
      modifier = Modifier.fillMaxWidth(),
      color = MaterialTheme.colorScheme.background,
      shadowElevation = 3.dp) {
        Column {
          // Location bar
          Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically) {
                Column {
                  Row {
                    Text(text = "Ecublens", style = MaterialTheme.typography.titleMedium)

                    IconButton(onClick = { /*TODO*/}, modifier = Modifier.size(24.dp)) {
                      Icon(
                          Icons.Outlined.KeyboardArrowDown,
                          contentDescription = "Filter",
                          modifier = Modifier.size(24.dp))
                    }
                  }

                  Text(text = "Less than 3 km", style = MaterialTheme.typography.bodySmall)
                }
              }

          // Search bar with toggle buttons
          Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
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
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
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
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                  Text("Filtering by:", style = MaterialTheme.typography.bodyMedium)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                      text = "Relevance",
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
fun ActivitiesDisplay(discoveryScreenViewModel: DiscoveryScreenViewModel = hiltViewModel()) {
  val activities = discoveryScreenViewModel.climbingActivities
  for (a in activities) {
    Card(
        modifier =
            Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .clickable(onClick = { /* Handle click */}),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    ) {
      Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = a.getActivityType().toString(),
                  modifier = Modifier.padding(4.dp).weight(1f),
                  color = Color.Blue)
              Icon(
                  imageVector = Icons.Default.FavoriteBorder,
                  contentDescription = "Favorite",
                  tint = Color.Gray)
            }
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
              Text(text = a.locationName ?: "Unnamed Activity", fontWeight = FontWeight.Bold)
            }

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
              Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = "Rating",
                  tint = MaterialTheme.colorScheme.primary)
              Text(text = "4.3")
              Text(text = "Medium")
              Text(text = "1,5 km")
            }
      }
    }
  }
}
