package com.lastaoutdoor.lasta.data.model.map

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.model.activity.ActivityType

// Marker Hiking activities
// @param name: the name of the marker
// @param position: the latitude and longitude of the marker
// @param description: the description of the marker
// @param icon: the icon of the marker
// @param activity: the activity Object of the marker
interface Marker {
  val name: String
  val position: LatLng
  val description: String
  val icon: BitmapDescriptor
  val activity: ActivityType
}

// Marker Climbing activities
// @param name: the name of the marker
// @param position: the latitude and longitude of the marker
// @param description: the description of the marker
// @param icon: the icon of the marker
// @param activity: returns ActivityType.CLIMBING
data class ClimbingMarker(
    override val name: String,
    override val position: LatLng,
    override val description: String,
    override val icon: BitmapDescriptor,
    override val activity: ActivityType = ActivityType.CLIMBING
) : Marker

// Marker Hiking activities
// @param name: the name of the marker
// @param position: the latitude and longitude of the marker
// @param description: the description of the marker
// @param icon: the icon of the marker
// @param activity: returns ActivityType.HIKING
data class HikingMarker(
    override val name: String,
    override val position: LatLng,
    override val description: String,
    override val icon: BitmapDescriptor,
    override val activity: ActivityType = ActivityType.HIKING
) : Marker
