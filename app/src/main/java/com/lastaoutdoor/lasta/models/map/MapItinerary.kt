package com.lastaoutdoor.lasta.models.map

import com.google.android.gms.maps.model.LatLng

data class MapItinerary(val id: Long, val name: String, val points: List<LatLng>)
