package com.lastaoutdoor.lasta.data.api

import com.lastaoutdoor.lasta.models.api.NodeWay
import com.lastaoutdoor.lasta.models.api.Relation
import com.lastaoutdoor.lasta.repository.api.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response

class FakeActivityRepository : ActivityRepository {

  // 2 arraylists simulating the response from the API
  /*private var climbingResponse: ArrayList<Node> = ArrayList()
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

  override fun getClimbingActivitiesNode(range: Int, lat: Double, lon: Double): APIResponse<Node> {

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

  // We do not need it right know
  override fun getClimbingActivitiesWay(range: Int, lat: Double, lon: Double): APIResponse<Way> {
    // This is not used yet in the code
    return APIResponse(1.0f, emptyList())
  }

  // We do not need it right know
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

  override suspend fun getHikingRoutesInfo(
      range: Int,
      lat: Double,
      lon: Double
  ): Response<List<Relation>> {
    TODO("Not yet implemented")
  }

  override suspend fun getHikingRouteById(id: Long): Response<Relation> {
    TODO("Not yet implemented")
  }

  override suspend fun getBikingRoutesInfo(
      range: Int,
      lat: Double,
      lon: Double
  ): Response<List<Relation>> {
    TODO("Not yet implemented")
  }

  override suspend fun getBikingRouteById(id: Long): Response<Relation> {
    TODO("Not yet implemented")
  }
}
