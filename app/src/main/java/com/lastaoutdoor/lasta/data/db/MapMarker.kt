package com.lastaoutdoor.lasta.data.db

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng

// Interface for markers on the map
// @param name: the name of the marker
// @param latitude: the latitude of the marker
// @param longitude: the longitude of the marker
interface Marker {
  val name: String
  val position: LatLng
}

// Marker for the activities
// @param name: the name of the marker
// @param latitude: the latitude of the marker
// @param longitude: the longitude of the marker
// @param description: the description of the marker
// @param activity: the activity Object of the marker
// @param icon: the icon of the marker
data class ActivityMarker(
    override val name: String,
    override val position: LatLng,
    val description: String,
    val activity: String, // TODO: change to activity type
    val icon: BitmapDescriptor // TODO: Will be inside the activity type
) : Marker
