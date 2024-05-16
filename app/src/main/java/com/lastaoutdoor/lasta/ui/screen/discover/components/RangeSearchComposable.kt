package com.lastaoutdoor.lasta.ui.screen.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.viewmodel.DiscoverDisplayType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSearchComposable(
    screen: DiscoverDisplayType,
    range: Double,
    localities: List<Pair<String, LatLng>>,
    selectedLocality: Pair<String, LatLng>,
    setRange: (Double) -> Unit,
    setSelectedLocality: (Pair<String, LatLng>) -> Unit,
    isRangePopup: Boolean,
    updateRange: (Double) -> Unit,
    updateInitialPosition: (LatLng) -> Unit,
    onDismissRequest: () -> Unit
) {

  // list view search popup
  if (isRangePopup && screen == DiscoverDisplayType.LIST) {

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth().testTag("searchOptions")) {
          Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                // Select the City
                Text(
                    text = LocalContext.current.getString(R.string.locality),
                    style =
                        TextStyle(
                            fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
                Spacer(modifier = Modifier.height(8.dp))
                // Dropdown to select the city
                LocalitySelectionDropdown(localities, selectedLocality, setSelectedLocality)
                // Select the distance radius
                Text(
                    text = LocalContext.current.getString(R.string.dist_radius),
                    style =
                        TextStyle(
                            fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
                Spacer(modifier = Modifier.height(8.dp))
                // Slider to select the range
                Row {
                  Slider(
                      value = range.toFloat(),
                      onValueChange = { setRange(it.toDouble().coerceIn(1000.0, 50000.0)) },
                      valueRange = 0f..50000f,
                      steps = 100,
                      modifier = Modifier.width(300.dp).testTag("listSearchOptionsSlider"))
                  Text(
                      // put range in km
                      text = "${(range / 1000).toInt()}km",
                      style = MaterialTheme.typography.bodyMedium,
                      modifier = Modifier.padding(8.dp))
                }

                // Button to apply the range
                ElevatedButton(
                    onClick = {
                      updateRange(range)
                      updateInitialPosition(selectedLocality.second)
                      onDismissRequest()
                    },
                    modifier =
                        Modifier.width(305.dp)
                            .height(48.dp)
                            .testTag("listSearchOptionsApplyButton"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                      Text(
                          LocalContext.current.getString(R.string.search),
                          style =
                              TextStyle(
                                  fontSize = 22.sp,
                                  lineHeight = 28.sp,
                                  fontWeight = FontWeight(400),
                              ))
                    }
              }
        }
  }

  // map range search popup
  if (isRangePopup && screen == DiscoverDisplayType.MAP) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth().testTag("rangeSearch")) {
          Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = LocalContext.current.getString(R.string.select_dist_radius),
                    style =
                        TextStyle(
                            fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
                // Slider to select the range
                Row {
                  Slider(
                      value = range.toFloat(),
                      onValueChange = { setRange(it.toDouble().coerceIn(1000.0, 50000.0)) },
                      valueRange = 0f..50000f,
                      steps = 100,
                      modifier = Modifier.width(300.dp).testTag("mapRangeSearchSlider"))
                  Text(
                      // put range in km
                      text = "${(range / 1000).toInt()}km",
                      style = MaterialTheme.typography.bodyMedium,
                      modifier = Modifier.padding(8.dp))
                }

                // Search bar to search by locality
                Row {
                  Text(
                      text = LocalContext.current.getString(R.string.locality),
                      style =
                          TextStyle(
                              fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
                  Spacer(modifier = Modifier.height(8.dp))
                  // Dropdown to select the city
                  LocalitySelectionDropdown(localities, selectedLocality, setSelectedLocality)
                }
                Spacer(modifier = Modifier.height(12.dp))

                // option to use current location with blue text and location icon
                Row(
                    modifier =
                        Modifier.clickable { /*TODO SET SELECTEDLOCALITY TO CURRENT POSITION */}) {
                      Icon(
                          imageVector = Icons.Default.LocationOn,
                          contentDescription = "Settings",
                          tint = MaterialTheme.colorScheme.primary)
                      Text(
                          text = LocalContext.current.getString(R.string.use_curr_loc),
                          style =
                              TextStyle(
                                  fontSize = 16.sp,
                                  lineHeight = 24.sp,
                                  fontWeight = FontWeight(500),
                                  color = PrimaryBlue))
                    }

                Spacer(modifier = Modifier.height(12.dp))

                // Button to apply the range
                ElevatedButton(
                    onClick = {
                      updateRange(range)
                      updateInitialPosition(selectedLocality.second)
                      onDismissRequest()
                    },
                    modifier =
                        Modifier.width(305.dp).height(48.dp).testTag("mapRangeSearchApplyButton"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                      Text(
                          LocalContext.current.getString(R.string.apply),
                          style =
                              TextStyle(
                                  fontSize = 22.sp,
                                  lineHeight = 28.sp,
                                  fontWeight = FontWeight(400),
                              ))
                    }
              }
        }
  }
}
