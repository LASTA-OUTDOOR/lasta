package com.lastaoutdoor.lasta.ui.screen.discover.components

import androidx.compose.ui.graphics.Path
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.ui.screen.map.mapScreen
import com.lastaoutdoor.lasta.ui.theme.PrimaryBlue
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenCallBacks
import com.lastaoutdoor.lasta.viewmodel.DiscoverScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSearchComposable(
    range: Double,
    selectedLocality: Pair<String, LatLng>,
    setRange: (Double) -> Unit,
    navigateBack: () -> Unit,
    discoverScreenState: DiscoverScreenState,
    discoverScreenCallBacks: DiscoverScreenCallBacks
) {

  // list view search popup
  Column(modifier = Modifier.fillMaxSize()) {
    TopAppBar(
        title = { Text(text = "hey") },
        actions = {
          IconButton(onClick = navigateBack) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        })

    Box(modifier = Modifier.weight(0.6f)) {
      mapScreen(
          discoverScreenState.mapState,
          discoverScreenState.initialPosition,
          discoverScreenState.initialZoom,
          discoverScreenCallBacks.updateMarkers,
          discoverScreenCallBacks.updateSelectedMarker,
          discoverScreenCallBacks.clearSelectedItinerary,
          discoverScreenState.selectedZoom,
          null,
          discoverScreenState.selectedItinerary,
          discoverScreenState.markerList,
          discoverScreenCallBacks.clearSelectedMarker)

      Canvas( modifier = Modifier
          .fillMaxSize()
          // ONLY ADD THIS
          .graphicsLayer {
              alpha = .99f
          }) {
        val width = size.width
        val height = size.height
        val circleRadius = 150.dp.toPx()
        val circleCenterX = width / 2
        val circleCenterY = height / 2

          // Destination
          drawRect(Color.Black.copy(alpha = 0.8f))

          // Source
          drawCircle(
              color = Color.Transparent,
              blendMode = BlendMode.Clear,
              radius = circleRadius,
          )
      }
    }

    DisplaySlider(
        range = range,
        setRange = setRange,
        selectedLocality = selectedLocality,
        navigateBack = navigateBack,
        discoverScreenCallBacks = discoverScreenCallBacks)
  }
}

@Composable
fun DisplaySlider(
    range: Double,
    setRange: (Double) -> Unit,
    selectedLocality: Pair<String, LatLng>,
    navigateBack: () -> Unit,
    discoverScreenCallBacks: DiscoverScreenCallBacks
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
              value = range.toFloat(),
              onValueChange = { setRange(it.toDouble().coerceIn(1000.0, 30000.0)) },
              valueRange = 0f..30000f,
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
              discoverScreenCallBacks.updateRange(range)
              discoverScreenCallBacks.updateInitialPosition(selectedLocality.second)
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
