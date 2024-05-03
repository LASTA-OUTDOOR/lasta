package com.lastaoutdoor.lasta.models.map

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.ActivityType

// Marker Hiking activities
// @param name: the name of the marker
// @param position: the latitude and longitude of the marker
// @param description: the description of the marker
// @param icon: the icon of the marker
// @param activity: the activity Object of the marker
data class Marker(
    val id: Long,
    val name: String,
    val position: LatLng,
    val description: String,
    val icon: Int,
    val activity: ActivityType
)
