package com.lastaoutdoor.lasta.data.api

import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.repository.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

// Class used to get OutdoorActivities from overpass API
@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class ActivityRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    ActivityRepository {

    override suspend fun getClimbingPointsInfo(range: Int, lat: Double, lon: Double): Response<List<NodeWay>> {
        return suspendCancellableCoroutine { continuation ->
            apiService.getClimbingPointsInfo(climbingInfoQuery(range, lat, lon)).enqueue(object : Callback<APIResponse<NodeWay>> {
                override fun onResponse(
                    call: Call<APIResponse<NodeWay>>,
                    response: retrofit2.Response<APIResponse<NodeWay>>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(Response.Success(response.body()!!.elements), onCancellation = null)
                    } else {
                        continuation.resume(Response.Failure(Exception("Error fetching the nodes info")), onCancellation = null)
                    }
                }

                override fun onFailure(call: Call<APIResponse<NodeWay>>, t: Throwable) {
                    continuation.resume(Response.Failure(t), onCancellation = null)
                }
            })
        }
    }

    override suspend fun getClimbingPointById(id: Long): Response<NodeWay> {
        return suspendCancellableCoroutine { continuation ->
            apiService.getClimbingPointById(climbingPointQuery(id)).enqueue(object : Callback<APIResponse<NodeWay>> {
                override fun onResponse(
                    call: Call<APIResponse<NodeWay>>,
                    response: retrofit2.Response<APIResponse<NodeWay>>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(Response.Success(response.body()!!.elements[0]), onCancellation = null)
                    } else {
                        continuation.resume(Response.Failure(Exception("Error fetching the ways info")), onCancellation = null)
                    }
                }

                override fun onFailure(call: Call<APIResponse<NodeWay>>, t: Throwable) {
                    continuation.resume(Response.Failure(t), onCancellation = null)
                }
            })
        }
    }

    private fun climbingInfoQuery(range: Int, lat: Double, lon: Double): String {
        return "[out:json][timeout:25];\n" +
            "(\n" +
            "  node(around: $range, $lat, $lon)[sport=climbing][name];\n" +
            "  way(around: $range, $lat, $lon)[sport=climbing][name];\n" +
            ");\n" +
            "out tags;\n"
    }

    private fun climbingPointQuery(id: Long): String {
        return "[out:json][timeout:25];\n" +
            "(\n" +
            "  nw($id);\n" +
            ");\n" +
            "out center;\n"
    }

  // Gets Nodes of type climbing
  /*override fun getClimbingActivitiesNode(
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
  }*/
}
