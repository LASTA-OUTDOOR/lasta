package com.lastaoutdoor.lasta.data.api

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.lastaoutdoor.lasta.data.model.api.Node
import com.lastaoutdoor.lasta.data.model.api.Relation
import com.lastaoutdoor.lasta.data.model.api.Way
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository

class FakeOutdoorActivityRepository : OutdoorActivityRepository {

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
