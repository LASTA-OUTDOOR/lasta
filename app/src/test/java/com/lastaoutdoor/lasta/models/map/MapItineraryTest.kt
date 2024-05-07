package com.lastaoutdoor.lasta.models.map

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.map.MapItinerary
import org.junit.Test

class MapItineraryTest {
  @Test
  fun mapIt() {
    val mp = MapItinerary(0, "name", emptyList())
    assert(mp.id == 0L && mp.name == "name" && mp.points == emptyList<LatLng>())
  }
}
