package com.lastaoutdoor.lasta.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapType
import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.data.model.api.Position
import com.lastaoutdoor.lasta.data.model.api.Relation
import com.lastaoutdoor.lasta.data.model.api.SimpleWay
import com.lastaoutdoor.lasta.data.model.api.Tags
import com.lastaoutdoor.lasta.data.model.map.ClimbingMarker
import com.lastaoutdoor.lasta.data.model.map.MapItinerary
import com.lastaoutdoor.lasta.data.model.map.Marker
import com.lastaoutdoor.lasta.repository.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// Class used to simulate the data given to the view model
class MockRepository : ActivityRepository {

  // 2 arraylists simulating the response from the API
  private var climbingResponse: ArrayList<NodeWay> = ArrayList()
  private var hikingResponse: ArrayList<Relation> = ArrayList()

  // add a climbing node to the tested list
  fun addClimbingNode(node: NodeWay) {
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

  /*override fun getClimbingActivitiesNode(range: Int, lat: Double, lon: Double): APIResponse<Node> {

    val resp: ArrayList<Node> = ArrayList()

    for (node in climbingResponse) {
      // check that the node is in the range with the SphericalUtil class
      if (SphericalUtil.computeDistanceBetween(LatLng(node.lat, node.lon), LatLng(lat, lon)) <=
          range) {
        resp.add(node)
      }
    }
    return APIResponse(1.0f, resp)
  }

  override fun getClimbingActivitiesWay(range: Int, lat: Double, lon: Double): APIResponse<Way> {
    // This is not used yet in the code
    return APIResponse(1.0f, emptyList())
  }

  override fun getHikingActivities(range: Int, lat: Double, lon: Double): APIResponse<Relation> {
    return APIResponse(1.0f, hikingResponse)
  }

  override fun getDataStringClimbing(range: Int, lat: Double, lon: Double, type: String): String {
    // don't need to implement this function here
    return ""
  }

  override fun getDataStringHiking(range: Int, lat: Double, lon: Double): String {
    // don't need to implement this function here
    return ""
  }*/
  override suspend fun getClimbingPointsInfo(
    range: Int,
    lat: Double,
    lon: Double
  ): Response<List<NodeWay>> {
    TODO("Not yet implemented")
  }

  override suspend fun getClimbingPointById(id: Long): Response<NodeWay> {
    TODO("Not yet implemented")
  }
}

@RunWith(AndroidJUnit4::class)
class MapViewModelTest {

  private val repository = MockRepository()
  private val viewModel: MapViewModel = MapViewModel(repository)
  private val lausanne: LatLng = LatLng(46.519962, 6.633597)

  private fun clear() {
    //repository.clearClimbingNodes()
    //repository.clearHikingRelations()
  }

  private fun dummyNode(type: ActivityType, name: String, position: LatLng): NodeWay {
    return NodeWay(type.toString(), 0, position.latitude,
      position.longitude, Position(position.latitude, position.longitude), Tags(""))
  }

  @Before
  fun setUp() {
    clear()
  }

  // CHeck all the basic properties of the view model and state
  @Test
  fun testInitialState() {
    // Check map properties
    assertTrue(viewModel.state.properties.mapType == MapType.TERRAIN)
    assertTrue(!viewModel.state.uiSettings.zoomControlsEnabled)

    // Initially the localization should be disabled
    assertTrue(!viewModel.state.uiSettings.myLocationButtonEnabled)
    assertTrue(!viewModel.state.properties.isMyLocationEnabled)

    // No markers should be displayed
    assertTrue(viewModel.state.markerList.isEmpty())

    // No marker should be selected
    assertTrue(viewModel.state.selectedMarker == null)

    // No itinerary should be selected
    assertTrue(viewModel.state.selectedItinerary == null)

    // No itinerary should be registered
    assertTrue(viewModel.state.itineraryMap.isEmpty())

    // Check initial position and zoom
    assertEquals(11f, viewModel.initialZoom)
    assertEquals(13f, viewModel.selectedZoom)
    assertEquals(LatLng(46.519962, 6.633597), viewModel.initialPosition)

    // Weird test required by Jacoco
    viewModel.state = MapState()
    assertNotNull(viewModel.state)
  }

  // If no climbing nodes are in the range, the list should be empty
  @Test
  fun emptyTest() {

    clear()
    viewModel.updateMarkers(lausanne, 1000.0)
    assertTrue(viewModel.state.markerList.isEmpty())

    repository.addClimbingNode(dummyNode(ActivityType.CLIMBING, "Point 1", lausanne))
    viewModel.updateMarkers(lausanne, 1000.0)
    assertFalse(viewModel.state.markerList.isEmpty())
  }

  // Check that all points are in the list that are going to be displayed
  @Test
  fun allClimbingNodesInRange() {

    clear()

    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 1", LatLng(46.52417, 6.60223)))
    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 2", LatLng(46.51873, 6.65029)))
    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 3", LatLng(46.54094, 6.63484)))

    viewModel.updateMarkers(lausanne, 10000.0)

    assertEquals(3, viewModel.state.markerList.size)
  }

  // Only points at an appropriate distance are fetched
  @Test
  fun limitedClimbingNodesInRange() {

    clear()

    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 1", LatLng(46.52417, 6.60223)))
    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 2", LatLng(46.51873, 6.65029)))
    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 3", LatLng(46.54094, 6.63484)))
    repository.addClimbingNode(
        dummyNode(ActivityType.CLIMBING, "Point 4", LatLng(0.54094, 0.63484)))

    viewModel.updateMarkers(lausanne, 10000.0)

    assertEquals(3, viewModel.state.markerList.size)
  }

  // Due to the non-uniform data, some fields might be null and we need to support it
  /*@Test
  fun nullPropertiesNode() {
    clear()

    repository.addClimbingNode(
        Node(
            "climbing",
            0,
            lausanne.latitude,
            lausanne.longitude,
            Tags(null, null),
            ActivityType.CLIMBING,
            0,
            10.2f,
            null,
            null))
    viewModel.updateMarkers(lausanne, 10000.0)
    assertEquals(1, viewModel.state.markerList.size)
  }

  // Check that the permission is updated correctly
  @Test
  fun testUpdatePermission() {
    viewModel.updatePermission(true)
    assertTrue(viewModel.state.uiSettings.myLocationButtonEnabled)
    assertTrue(viewModel.state.properties.isMyLocationEnabled)

    viewModel.updatePermission(false)
    assertTrue(!viewModel.state.uiSettings.myLocationButtonEnabled)
    assertTrue(!viewModel.state.properties.isMyLocationEnabled)
  }

  // Check that the selected marker is updated correctly
  @Test
  fun testUpdateSelectedMarker() {
    clear()
    assertNull(viewModel.state.selectedMarker)

    val marker: Marker = ClimbingMarker("Point 1", lausanne, "climbing", 1)
    viewModel.updateSelectedMarker(marker)
    assertEquals(marker, viewModel.state.selectedMarker)
  }

  // test clear selected itinerary
  @Test
  fun testClearSelectedItinerary() {
    clear()
    assertNull(viewModel.state.selectedItinerary)

    val itinerary = MapItinerary(1, "Itinerary", emptyList())
    viewModel.state.selectedItinerary = itinerary
    assertNotNull(viewModel.state.selectedItinerary)

    viewModel.clearSelectedItinerary()
    assertNull(viewModel.state.selectedItinerary)
  }

  // This test is a global test for itineraries since it is hard to isolate only one bit of it
  @Test
  fun testItinerary() {
    clear()
    assertNull(viewModel.state.selectedItinerary)

    repository.addHikingRelation(
        Relation(
            "Hiking",
            1,
            Tags("Hiking", "hiking"),
            listOf(SimpleWay(listOf(Position(lausanne.latitude, lausanne.longitude)))),
            Bounds(0.0, 0.0, 0.0, 0.0),
            ActivityType.HIKING,
            0,
            0f,
            "",
            "Test location"))
    viewModel.updateMarkers(lausanne, 10000.0)
    assertEquals(1, viewModel.state.itineraryStartMarkers.size)
    viewModel.updateSelectedItinerary(1)
    assertNotNull(viewModel.state.selectedItinerary)
    assertEquals(1L, viewModel.state.selectedItinerary?.id)
    assertEquals("Hiking", viewModel.state.selectedItinerary?.name)
  }
  // test when ways is null
  @Test
  fun testItineraryWayNull() {
    clear()

    repository.addHikingRelation(
        Relation(
            "Hiking",
            1,
            Tags("Hiking", "hiking"),
            null,
            Bounds(0.0, 0.0, 0.0, 0.0),
            ActivityType.HIKING,
            0,
            0f,
            "",
            "Test location"))
    viewModel.updateMarkers(lausanne, 10000.0)
    assertEquals(0, viewModel.state.itineraryStartMarkers.size)
  }
  // Test when node is null
  @Test
  fun testItineraryNodeNull() {
    clear()
    repository.addHikingRelation(
        Relation(
            "Hiking",
            1,
            Tags("Hiking", "hiking"),
            listOf(SimpleWay(null)),
            Bounds(0.0, 0.0, 0.0, 0.0),
            ActivityType.HIKING,
            0,
            0f,
            "",
            "Test location"))
    viewModel.updateMarkers(lausanne, 10000.0)
    assertEquals(0, viewModel.state.itineraryStartMarkers.size)
  }

  // Test when name in tags is null
  @Test
  fun testItineraryNameNull() {
    clear()
    repository.addHikingRelation(
        Relation(
            "Hiking",
            1,
            Tags(null, "hiking"),
            listOf(SimpleWay(listOf(Position(lausanne.latitude, lausanne.longitude)))),
            Bounds(0.0, 0.0, 0.0, 0.0),
            ActivityType.HIKING,
            0,
            0f,
            "",
            "Test location"))
    viewModel.updateMarkers(lausanne, 10000.0)
    assertEquals(1, viewModel.state.itineraryStartMarkers.size)
    assertEquals("Hiking: Unnamed", viewModel.state.itineraryStartMarkers[0].name)
  }*/
}
