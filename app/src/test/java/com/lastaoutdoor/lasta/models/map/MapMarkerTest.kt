package com.lastaoutdoor.lasta.models.map

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.ActivityType
import org.junit.Test

class MapMarkerTest {
  @Test
  fun markerTest() {
    val m = Marker(0, "Test Market", LatLng(0.0, 0.0), "Description Test", 0, ActivityType.HIKING)
    assert(m.id == 0L)
    assert(m.name == "Test Market")
    assert(m.position == LatLng(0.0, 0.0))
    assert(m.description == "Description Test")
    assert(m.icon == 0)
    assert(m.activity == ActivityType.HIKING)
  }
}
