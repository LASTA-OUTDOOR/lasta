package com.lastaoutdoor.lasta.repository

import com.lastaoutdoor.lasta.data.api.ApiService
import com.lastaoutdoor.lasta.data.api.OutdoorActivityResponse
import com.lastaoutdoor.lasta.data.model.ActivityType
import com.lastaoutdoor.lasta.data.model.Node
import com.lastaoutdoor.lasta.data.model.Relation
import com.lastaoutdoor.lasta.data.model.Way
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Class used to get OutdoorActivities from overpass API
class OutdoorActivityRepository(/*context: Context*/ ) {
  // creates instance of ApiService to execute calls
  private val apiService: ApiService by lazy {
    Retrofit.Builder()
        .baseUrl("https://overpass-api.de/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
  }
  // Gets Nodes of type climbing
  suspend fun getClimbingActivitiesNode(
      range: Int,
      lat: Float,
      lon: Float
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

  suspend fun getClimbingActivitiesWay(
      range: Int,
      lat: Float,
      lon: Float
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

  suspend fun getHikingActivities(
      range: Int,
      lat: Float,
      lon: Float
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

  fun getDataStringClimbing(range: Int, lat: Float, lon: Float, type: String): String {
    return "[out:json];$type(around:$range,$lat,$lon)[sport=climbing];out geom;"
  }

  fun getDataStringHiking(range: Int, lat: Float, lon: Float): String {
    return "[out:json];relation(around:$range,$lat,$lon)[route][route=\"hiking\"];out geom;"
  }
  /**
   * private val database = OutdoorActivityDatabase.getInstance(context) private val
   * outdoorActivityDao = database.outdoorActivityDao() @@ -27,4 +69,9 @@ class
   * OutdoorActivityRepository(context: Context) { suspend fun delete(outdoorActivity:
   * OutdoorActivity) { outdoorActivityDao.delete(outdoorActivity) }
   */
}
// main function to test calls
suspend fun main() {
  val f = OutdoorActivityRepository()
  val q = f.getHikingActivities(1000, 47.447227f, 7.617517f)
  println(q.elements.toString())
}
