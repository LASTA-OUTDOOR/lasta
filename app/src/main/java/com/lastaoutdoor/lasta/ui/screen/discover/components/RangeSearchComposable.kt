package com.lastaoutdoor.lasta.ui.screen.discover.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.screen.map.mapScreen
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.utils.radiusToZoom
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenCallBacks
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSearchComposable(
    selectedLocality: Pair<String, LatLng>,
    setRange: (Double) -> Unit,
    navigateBack: () -> Unit,
    discoverScreenState: DiscoverScreenState,
    discoverScreenCallBacks: DiscoverScreenCallBacks
) {
  val configuration = LocalConfiguration.current
  val screenWidthDp = configuration.screenWidthDp.dp
  val screenWidthPx = screenWidthDp.toPx() // Convert dp to pixels

  val initialRadius = 10000.0

  val initialZoom = radiusToZoom(initialRadius, discoverScreenState.centerPoint.latitude)

  var radius by remember { mutableDoubleStateOf(initialRadius) }

  // list view search popup
  Column(modifier = Modifier.fillMaxSize()) {
    TopAppBar(
        title = { Text(text = "hey") },
        actions = {
          IconButton(onClick = navigateBack) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        })

    Box(modifier = Modifier.weight(0.5f)) {
      val cameraPositionState =
          mapScreen(
              discoverScreenState.mapState,
              discoverScreenState.initialPosition,
              initialZoom,
              discoverScreenCallBacks.updateMarkers,
              {},
              {},
              discoverScreenState.selectedZoom,
              null,
              null,
              discoverScreenState.markerList,
              discoverScreenCallBacks.clearSelectedMarker,
              true)

      Canvas(
          modifier =
              Modifier.fillMaxSize()
                  // ONLY ADD THIS
                  .graphicsLayer { alpha = .99f }) {

            // Destination
            drawRect(Color.Black.copy(alpha = 0.8f))

            // Source
            drawCircle(
                color = Color.Transparent,
                blendMode = BlendMode.Clear,
                radius = screenWidthPx / 2 - 16.dp.toPx())
          }

      // Here you can add controls or UI elements to adjust zoom and radius
      // Example: Update the camera position when the radius changes
      LaunchedEffect(radius) {
        val newZoom = radiusToZoom(radius, discoverScreenState.centerPoint.latitude)
        cameraPositionState(CameraUpdateFactory.zoomTo(newZoom))
      }
    }

    DisplaySlider(
        radius = radius,
        setRange = setRange,
        selectedLocality = selectedLocality,
        navigateBack = navigateBack,
        discoverScreenCallBacks = discoverScreenCallBacks,
        updateRadius = { newRadius -> radius = newRadius })
  }
}

@Composable
fun DisplaySlider(
    radius: Double,
    setRange: (Double) -> Unit,
    selectedLocality: Pair<String, LatLng>,
    navigateBack: () -> Unit,
    discoverScreenCallBacks: DiscoverScreenCallBacks,
    updateRadius: (Double) -> Unit
) {
  Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("searchOptions")) {
        // Select the City
        Text(
            text = LocalContext.current.getString(R.string.locality),
            style = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
        Spacer(modifier = Modifier.height(8.dp))
        // Dropdown to select the city
        // Select the distance radius
        Text(
            text = LocalContext.current.getString(R.string.dist_radius),
            style = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight(500)))
        Spacer(modifier = Modifier.height(8.dp))
        // Slider to select the range
        Row {
          Slider(
              value = radius.toFloat(),
              onValueChange = { updateRadius(it.toDouble()) },
              valueRange = 10000f..50000f,
              steps = 1000,
              modifier = Modifier.width(300.dp).testTag("listSearchOptionsSlider"))
          Text(
              // put range in km
              text = "${(radius / 1000).toInt()}km",
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.padding(8.dp))
        }

        // Button to apply the range
        ElevatedButton(
            onClick = {
              discoverScreenCallBacks.updateRange(radius)
              // discoverScreenCallBacks.updateInitialPosition(selectedLocality.second)
              navigateBack()
            },
            modifier = Modifier.width(305.dp).height(48.dp).testTag("listSearchOptionsApplyButton"),
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

@Composable
fun Dp.toPx(): Float {
  val density = LocalDensity.current
  return with(density) { this@toPx.toPx() }
}
