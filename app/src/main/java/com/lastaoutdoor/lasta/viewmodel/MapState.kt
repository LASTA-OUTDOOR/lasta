package com.lastaoutdoor.lasta.viewmodel

import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.lastaoutdoor.lasta.models.map.HikingMarker
import com.lastaoutdoor.lasta.models.map.MapItinerary
import com.lastaoutdoor.lasta.models.map.Marker

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

  // List of hiking markers to display on the map (first of itinerary)
  var itineraryStartMarkers: List<HikingMarker> = emptyList()

  // Itinerary to display : relation id points to the itinerary
  var itineraryMap: Map<Long, MapItinerary> = emptyMap()

  // Displayed itinerary
  var selectedItinerary: MapItinerary? = null

  // The marker displayed in the more info bottom sheet
  var selectedMarker: Marker? = null
}
