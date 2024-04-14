package com.lastaoutdoor.lasta.viewmodel

import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.lastaoutdoor.lasta.data.model.map.MapItinerary
import com.lastaoutdoor.lasta.data.model.map.Marker

// state for the map on the mapScreen
class MapState {

  // Properties of the displayed map, only one field now, but can be expanded
  var properties: MapProperties =
      MapProperties(mapType = MapType.TERRAIN, isMyLocationEnabled = false)

  // UI settings of the map -> Want we want to show
  var uiSettings: MapUiSettings =
      MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)

  // List of markers to display on the map
  var markerList: List<Marker> = emptyList()

  // Itinerary to display
  var itineraryList: List<MapItinerary> = emptyList()

  // The marker displayed in the more info bottom sheet
  var selectedMarker: Marker? = null
}
