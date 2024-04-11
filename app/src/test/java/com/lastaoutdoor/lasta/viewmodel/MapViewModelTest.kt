package com.lastaoutdoor.lasta.viewmodel

import com.lastaoutdoor.lasta.data.api.OutdoorActivityResponse
import com.lastaoutdoor.lasta.data.model.api.Node
import com.lastaoutdoor.lasta.data.model.api.Relation
import com.lastaoutdoor.lasta.data.model.api.Way
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository

//Class used to simulate the data given to the view model
class MockRepository : OutdoorActivityRepository {
  override fun getClimbingActivitiesNode(
    range: Int,
    lat: Double,
    lon: Double
  ): OutdoorActivityResponse<Node> {
    TODO("Not yet implemented")
  }

  override fun getClimbingActivitiesWay(
    range: Int,
    lat: Double,
    lon: Double
  ): OutdoorActivityResponse<Way> {
    TODO("Not yet implemented")
  }

  override fun getHikingActivities(
    range: Int,
    lat: Double,
    lon: Double
  ): OutdoorActivityResponse<Relation> {
    TODO("Not yet implemented")
  }

  override fun getDataStringClimbing(range: Int, lat: Double, lon: Double, type: String): String {
    //don't need to implement this function here
    return ""
  }

  override fun getDataStringHiking(range: Int, lat: Double, lon: Double): String {
    //don't need to implement this function here
    return ""
  }

}

class MapViewModelTest {

  val repository = MockRepository()
  val viewModel : MapViewModel = MapViewModel(repository)
  @Before fun setUp() {

  }

  @Test
  fun getState() {
    assert(2 + 2 == 4)
  }
}
