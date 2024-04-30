package com.lastaoutdoor.lasta.models.map

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.ActivityType

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
  val icon: Int
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
    override val icon: Int,
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
    override val icon: Int,
    val id: Long,
    override val activity: ActivityType = ActivityType.HIKING
) : Marker