package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.api.OutdoorActivityResponse
import com.lastaoutdoor.lasta.data.model.Node
import com.lastaoutdoor.lasta.data.model.Relation
import com.lastaoutdoor.lasta.data.model.Way

// Interface used to get OutdoorActivities from overpass API
interface OutdoorActivityRepositoryIn {

  fun getClimbingActivitiesNode(range: Int, lat: Double, lon: Double): OutdoorActivityResponse<Node>

  fun getClimbingActivitiesWay(range: Int, lat: Double, lon: Double): OutdoorActivityResponse<Way>

  fun getHikingActivities(range: Int, lat: Double, lon: Double): OutdoorActivityResponse<Relation>

  fun getDataStringClimbing(range: Int, lat: Double, lon: Double, type: String): String

  fun getDataStringHiking(range: Int, lat: Double, lon: Double): String
}
