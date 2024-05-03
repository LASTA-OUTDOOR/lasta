package com.lastaoutdoor.lasta.viewmodel

import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import org.junit.Assert.*
import org.junit.Test

class MapStateTest {

  @Test
  fun testMapStateInitialization() {
    // Initialize the MapState object
    val mapState = MapState()

    // Check if the object is not null
    assertNotNull("MapState object should not be null", mapState)

    // Check default values of MapProperties
    assertNotNull("Map properties should not be null", mapState.properties)
    assertEquals(MapType.TERRAIN, mapState.properties.mapType)
    assertFalse(mapState.properties.isMyLocationEnabled)

    // Check default values of MapUiSettings
    assertNotNull("UI settings should not be null", mapState.uiSettings)
    assertFalse(
        "Zoom controls should be disabled by default", mapState.uiSettings.zoomControlsEnabled)
    assertFalse(
        "My location button should be disabled by default",
        mapState.uiSettings.myLocationButtonEnabled)

    mapState.properties = MapProperties(mapType = MapType.NORMAL, isMyLocationEnabled = false)
    mapState.uiSettings = MapUiSettings(zoomControlsEnabled = true, myLocationButtonEnabled = true)
    assertEquals(MapType.NORMAL, mapState.properties.mapType)
    assertTrue(mapState.uiSettings.zoomControlsEnabled)
    assertTrue(mapState.uiSettings.zoomControlsEnabled)
    assertTrue(mapState.uiSettings.myLocationButtonEnabled)
  }
}
