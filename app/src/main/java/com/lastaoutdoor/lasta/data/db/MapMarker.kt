package com.lastaoutdoor.lasta.data.db

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.model.ActivityType

// Interface for markers on the map
// @param name: the name of the marker
// @param latitude: the latitude of the marker
// @param longitude: the longitude of the marker
interface Marker {
  val name: String
  val position: LatLng
  val activity : ActivityType
  val description : String
}

// Marker for the activities
// @param name: the name of the marker
// @param latitude: the latitude of the marker
// @param longitude: the longitude of the marker
// @param description: the description of the marker
// @param activity: the activity Object of the marker
// @param icon: the icon of the marker
data class ClimbingMarker(
    override val name: String,
    override val position: LatLng,
    override val description: String,
    val icon: BitmapDescriptor, // TODO: Will be inside the activity type
    override val activity: ActivityType = ActivityType.HIKING
) : Marker
