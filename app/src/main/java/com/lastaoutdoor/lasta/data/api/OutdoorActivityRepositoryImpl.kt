package com.lastaoutdoor.lasta.data.api

import com.lastaoutdoor.lasta.data.model.activity.ActivityType
import com.lastaoutdoor.lasta.data.model.api.Node
import com.lastaoutdoor.lasta.data.model.api.Relation
import com.lastaoutdoor.lasta.data.model.api.Way
import com.lastaoutdoor.lasta.repository.OutdoorActivityRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

// Class used to get OutdoorActivities from overpass API
@Singleton
class OutdoorActivityRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    OutdoorActivityRepository {

  // Gets Nodes of type climbing
  override fun getClimbingActivitiesNode(
      range: Int,
      lat: Double,
      lon: Double
  ): OutdoorActivityResponse<Node> {
    return try {
      val call = apiService.getNode(getDataStringClimbing(range, lat, lon, "node"))
      val response = call.execute()
      OutdoorActivityResponse(
          response.body()!!.version,
          (response.body()!!.elements).map {
            it.activityType = ActivityType.CLIMBING
            it
          })
    } catch (e: Exception) {
      e.printStackTrace()
      OutdoorActivityResponse(0F, emptyList())
    }
  }

  override fun getClimbingActivitiesWay(
      range: Int,
      lat: Double,
      lon: Double
  ): OutdoorActivityResponse<Way> {
    return try {
      val call = apiService.getWay(getDataStringClimbing(range, lat, lon, "way"))
      val response = call.execute()
      OutdoorActivityResponse(
          response.body()!!.version,
          (response.body()!!.elements).map {
            it.activityType = ActivityType.CLIMBING
            it
          })
    } catch (e: Exception) {
      e.printStackTrace()
      OutdoorActivityResponse(0F, emptyList())
    }
  }

  override fun getHikingActivities(
      range: Int,
      lat: Double,
      lon: Double
  ): OutdoorActivityResponse<Relation> {
    return try {
      val call = apiService.getRelation(getDataStringHiking(range, lat, lon))
      val pr = call.execute()
      OutdoorActivityResponse(
          pr.body()!!.version,
          (pr.body()!!.elements).map {
            it.activityType = ActivityType.HIKING
            it
          })
    } catch (e: Exception) {
      e.printStackTrace()
      OutdoorActivityResponse(0F, emptyList())
    }
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
