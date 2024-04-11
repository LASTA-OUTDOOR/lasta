package com.lastaoutdoor.lasta.viewmodel

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.lastaoutdoor.lasta.data.api.OutdoorActivityResponse
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.api.Node
import com.lastaoutdoor.lasta.data.model.api.Relation
import com.lastaoutdoor.lasta.data.model.api.Tags
import com.lastaoutdoor.lasta.data.model.api.Way
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// Class used to simulate the data given to the view model
class MockRepository : OutdoorActivityRepository {

  // 2 arraylists simulating the response from the API
  private var climbingResponse: ArrayList<Node> = ArrayList()
  private var hikingResponse: ArrayList<Relation> = ArrayList()

  // add a climbing node to the tested list
  fun addClimbingNode(node: Node) {
    climbingResponse.add(node)
  }

  // clear the climbing nodes list
  fun clearClimbingNodes() {
    climbingResponse.clear()
  }

  // add a hiking relation to the tested list
  fun addHikingRelation(relation: Relation) {
    hikingResponse.add(relation)
  }

  // clear the hiking relations list
  fun clearHikingRelations() {
    hikingResponse.clear()
  }

  override fun getClimbingActivitiesNode(
      range: Int,
      lat: Double,
      lon: Double
  ): OutdoorActivityResponse<Node> {

    val resp: ArrayList<Node> = ArrayList()

    for (node in climbingResponse) {
      // check that the node is in the range with the SphericalUtil class
      if (SphericalUtil.computeDistanceBetween(LatLng(node.lat, node.lon), LatLng(lat, lon)) <=
          range) {
        resp.add(node)
      }
    }
    return OutdoorActivityResponse(1.0f, resp)
  }

  // We do not need it right know
  override fun getClimbingActivitiesWay(
      range: Int,
      lat: Double,
      lon: Double
  ): OutdoorActivityResponse<Way> {
    // This is not used yet in the code
    return OutdoorActivityResponse(1.0f, emptyList())
  }

  // We do not need it right know
  override fun getHikingActivities(
      range: Int,
      lat: Double,
      lon: Double
  ): OutdoorActivityResponse<Relation> {
    return OutdoorActivityResponse(1.0f, hikingResponse)
  }

  override fun getDataStringClimbing(range: Int, lat: Double, lon: Double, type: String): String {
    // don't need to implement this function here
    return ""
  }

  override fun getDataStringHiking(range: Int, lat: Double, lon: Double): String {
    // don't need to implement this function here
    return ""
  }
}

@RunWith(AndroidJUnit4::class)
class MapViewModelTest {

  private val repository = MockRepository()
  private val viewModel: MapViewModel = MapViewModel(repository)
  private val lausanne: LatLng = LatLng(46.519962, 6.633597)

  private fun clear() {
    repository.clearClimbingNodes()
    repository.clearHikingRelations()
  }

  private fun dummyNode(type: ActivityType, name: String, position: LatLng): Node {
    return Node(
        type.toString(),
        0,
        position.latitude,
        position.longitude,
        Tags(name, type.toString()),
        type,
        0,
        10.2f,
        "1",
        name)
  }

  @Before
  fun setUp() {
    // Robotelectric
    clear()
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    MapsInitializer.initialize(context)
  }

  @Test
  fun emptyTest() {

    clear()
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    MapsInitializer.initialize(context)
    viewModel.updateMarkers(lausanne, 1000.0)
    assertTrue(viewModel.state.markerList.isEmpty())
  }

  // add points are supposed to be fetched
  @Test
  fun allClimbingNodesInRange() {

    clear()
    MapsInitializer.initialize(getApplicationContext())

    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 1", LatLng(46.52417, 6.60223)))
    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 2", LatLng(46.51873, 6.65029)))
    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 3", LatLng(46.54094, 6.63484)))

    viewModel.updateMarkers(lausanne, 10000.0)

    assertEquals(3, viewModel.state.markerList.size)
  }
}
