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

  var uiSettings: MapUiSettings =
      MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)

  var markerList: List<Marker> = emptyList()

  var itineraryList: List<MapItinerary> = emptyList()
}
