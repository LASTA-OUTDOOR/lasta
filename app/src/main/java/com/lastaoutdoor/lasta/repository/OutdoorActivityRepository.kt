package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.api.ApiService
import com.lastaoutdoor.lasta.data.api.OutdoorActivityResponse
import com.lastaoutdoor.lasta.data.model.ActivityType
import com.lastaoutdoor.lasta.data.model.Node
import com.lastaoutdoor.lasta.data.model.Relation
import com.lastaoutdoor.lasta.data.model.Way
import javax.inject.Inject

// Class used to get OutdoorActivities from overpass API
class OutdoorActivityRepository
@Inject
constructor( // creates instance of ApiService to execute calls
    private val apiService: ApiService
) : OutdoorActivityRepositoryIn {
  // Gets Nodes of type climbing
  override fun getClimbingActivitiesNode(
      range: Int,
      lat: Double,
      lon: Double
  ): OutdoorActivityResponse<Node> {
    val call = apiService.getNode(getDataStringClimbing(range, lat, lon, "node"))
    val pr = call.execute()
    return OutdoorActivityResponse(
        pr.body()!!.version,
        (pr.body()!!.elements).map {
          it.setActivityType(ActivityType.CLIMBING)
          it
        })
  }

  override fun getClimbingActivitiesWay(
      range: Int,
      lat: Double,
      lon: Double
  ): OutdoorActivityResponse<Way> {
    val call = apiService.getWay(getDataStringClimbing(range, lat, lon, "way"))
    val pr = call.execute()
    return OutdoorActivityResponse(
        pr.body()!!.version,
        (pr.body()!!.elements).map {
          it.setActivityType(ActivityType.CLIMBING)
          it
        })
  }

  override fun getHikingActivities(
      range: Int,
      lat: Double,
      lon: Double
  ): OutdoorActivityResponse<Relation> {
    val call = apiService.getRelation(getDataStringHiking(range, lat, lon))
    val pr = call.execute()
    return OutdoorActivityResponse(
        pr.body()!!.version,
        (pr.body()!!.elements).map {
          it.setActivityType(ActivityType.HIKING)
          it
        })
  }

  override fun getDataStringClimbing(range: Int, lat: Double, lon: Double, type: String): String {
    return "[out:json];$type(around:$range,$lat,$lon)[sport=climbing];out geom;"
  }

  override fun getDataStringHiking(range: Int, lat: Double, lon: Double): String {
    return "[out:json];relation(around:$range,$lat,$lon)[route][route=\"hiking\"];out geom;"
  }
  /**
   * private val database = OutdoorActivityDatabase.getInstance(context) private val
   * outdoorActivityDao = database.outdoorActivityDao() @@ -27,4 +69,9 @@ class
   * OutdoorActivityRepository(context: Context) { suspend fun delete(outdoorActivity:
   * OutdoorActivity) { outdoorActivityDao.delete(outdoorActivity) }
   */
}
