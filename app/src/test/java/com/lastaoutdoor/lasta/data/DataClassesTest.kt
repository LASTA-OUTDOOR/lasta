package com.lastaoutdoor.lasta.data

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.map.HikingMarker
import com.lastaoutdoor.lasta.models.map.MapItinerary
import org.junit.Test

class DataClassesTest {
  @Test
  fun hikingMarker() {
    val hm = HikingMarker("name", LatLng(1.0, 1.0), "desc", 0, 4)
    assert(
        hm.name == "name" &&
            hm.position == LatLng(1.0, 1.0) &&
            hm.description == "desc" &&
            hm.icon == 0 &&
            hm.id == 4L)
  }


  @Test
  fun mapit() {
    val mp = MapItinerary(0, "name", emptyList())
    assert(mp.id == 0L && mp.name == "name" && mp.points == emptyList<LatLng>())
  }

  @Test
  fun climStyle() {
    val c2 = ClimbingStyle.INDOOR
    val c3 = ClimbingStyle.OUTDOOR
    assert(c3 != c2)
  }

  @Test
  fun diffToString() {
    assert(Difficulty.HARD.toString() == "Hard")
  }
}
