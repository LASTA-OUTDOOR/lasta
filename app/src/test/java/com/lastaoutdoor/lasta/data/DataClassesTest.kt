package com.lastaoutdoor.lasta.data

import com.google.android.gms.maps.model.LatLng
import com.lastaoutdoor.lasta.data.db.HikingField
import com.lastaoutdoor.lasta.data.model.activity.ClimbingStyle
import com.lastaoutdoor.lasta.data.model.activity.Difficulty
import com.lastaoutdoor.lasta.data.model.map.HikingMarker
import com.lastaoutdoor.lasta.data.model.map.MapItinerary
import com.lastaoutdoor.lasta.data.model.profile.Popularity
import org.junit.Test

class DataClassesTest {
    @Test
    fun hikingMarker(){
        val hm = HikingMarker("name", LatLng(1.0,1.0),"desc",4)
        assert(hm.name == "name" && hm.position == LatLng(1.0,1.0)
                && hm.description == "desc" && hm.icon == 4 )
    }
    @Test
    fun popularity(){
        val pop = Popularity(1.0f,1)
        assert(pop.rating == 1.0f && pop.numRatings == 1)
    }
    @Test
    fun mapit(){
        val mp = MapItinerary(0,"name", emptyList())
        assert(mp.id == 0L && mp.name == "name" && mp.points == emptyList<LatLng>())
    }
    @Test
    fun climStyle(){
        val c1 = ClimbingStyle.FREE_SOLO
        val c2 = ClimbingStyle.INDOOR
        val c3 = ClimbingStyle.OUTDOOR
        assert(c1 != c2 && c3 != c1)
    }
    @Test
    fun diffToString() {
        assert(Difficulty.DIFFICULT.toString() == "Difficult")
    }
    @Test
    fun hkFields(){
        val h1 = HikingField.AVG_SPEED_IN_KMH
        val h2 = HikingField.CALORIES_BURNED
        val h3 = HikingField.DISTANCE_IN_METERS
        val h4 = HikingField.TIME_FINISHED
        val h5 = HikingField.ELEVATION_CHANGE_IN_METERS
        val h6 = HikingField.TIME_STARTED
        assert(h1!= h2 && h3!= h4 && h5 != h6)
    }
}