package com.lastaoutdoor.lasta.data.api

import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.data.model.api.Relation
import com.lastaoutdoor.lasta.repository.ActivityRepository
import com.lastaoutdoor.lasta.utils.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback

/**
 * This is an implementation of the [ActivityRepository] interface. We define here the main methods
 * to fetch the data from the API. We use the [OSMApiService] to make the requests.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class ActivityRepositoryImpl @Inject constructor(private val osmApiService: OSMApiService) :
    ActivityRepository {

  override suspend fun getClimbingPointsInfo(
      range: Int,
      lat: Double,
      lon: Double
  ): Response<List<NodeWay>> {
    return suspendCancellableCoroutine { continuation ->
      osmApiService
          .getClimbingPointsInfo(climbingInfoQuery(range, lat, lon))
          .enqueue(
              object : Callback<APIResponse<NodeWay>> {
                override fun onResponse(
                    call: Call<APIResponse<NodeWay>>,
                    response: retrofit2.Response<APIResponse<NodeWay>>
                ) {
                  if (response.isSuccessful) {
                    continuation.resume(
                        Response.Success(response.body()!!.elements), onCancellation = null)
                  } else {
                    continuation.resume(
                        Response.Failure(Exception("Error fetching the nodes info")),
                        onCancellation = null)
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
      osmApiService
          .getClimbingPointById(climbingPointQuery(id))
          .enqueue(
              object : Callback<APIResponse<NodeWay>> {
                override fun onResponse(
                    call: Call<APIResponse<NodeWay>>,
                    response: retrofit2.Response<APIResponse<NodeWay>>
                ) {
                  if (response.isSuccessful) {
                    continuation.resume(
                        Response.Success(response.body()!!.elements[0]), onCancellation = null)
                  } else {
                    continuation.resume(
                        Response.Failure(Exception("Error fetching the ways info")),
                        onCancellation = null)
                  }
                }

                override fun onFailure(call: Call<APIResponse<NodeWay>>, t: Throwable) {
                  continuation.resume(Response.Failure(t), onCancellation = null)
                }
              })
    }
  }

  override suspend fun getHikingRoutesInfo(
      range: Int,
      lat: Double,
      lon: Double
  ): Response<List<Relation>> {
    return suspendCancellableCoroutine { continuation ->
      osmApiService
          .getHikingRoutesInfo(hikingInfoQuery(range, lat, lon))
          .enqueue(
              object : Callback<APIResponse<Relation>> {
                override fun onResponse(
                    call: Call<APIResponse<Relation>>,
                    response: retrofit2.Response<APIResponse<Relation>>
                ) {
                  if (response.isSuccessful) {
                    continuation.resume(
                        Response.Success(response.body()!!.elements), onCancellation = null)
                  } else {
                    continuation.resume(
                        Response.Failure(Exception("Error fetching the hiking routes info")),
                        onCancellation = null)
                  }
                }

                override fun onFailure(call: Call<APIResponse<Relation>>, t: Throwable) {
                  continuation.resume(Response.Failure(t), onCancellation = null)
                }
              })
    }
  }

  override suspend fun getHikingRouteById(id: Long): Response<Relation> {
    return suspendCancellableCoroutine { continuation ->
      osmApiService
          .getHikingRouteById(hikingRouteQuery(id))
          .enqueue(
              object : Callback<APIResponse<Relation>> {
                override fun onResponse(
                    call: Call<APIResponse<Relation>>,
                    response: retrofit2.Response<APIResponse<Relation>>
                ) {
                  if (response.isSuccessful) {
                    continuation.resume(
                        Response.Success(response.body()!!.elements[0]), onCancellation = null)
                  } else {
                    continuation.resume(
                        Response.Failure(Exception("Error fetching the hiking route info")),
                        onCancellation = null)
                  }
                }

                override fun onFailure(call: Call<APIResponse<Relation>>, t: Throwable) {
                  continuation.resume(Response.Failure(t), onCancellation = null)
                }
              })
    }
  }

  override suspend fun getBikingRoutesInfo(
      range: Int,
      lat: Double,
      lon: Double
  ): Response<List<Relation>> {
    return suspendCancellableCoroutine { continuation ->
      osmApiService
          .getBikingRoutesInfo(bikingInfoQuery(range, lat, lon))
          .enqueue(
              object : Callback<APIResponse<Relation>> {
                override fun onResponse(
                    call: Call<APIResponse<Relation>>,
                    response: retrofit2.Response<APIResponse<Relation>>
                ) {
                  if (response.isSuccessful) {
                    continuation.resume(
                        Response.Success(response.body()!!.elements), onCancellation = null)
                  } else {
                    continuation.resume(
                        Response.Failure(Exception("Error fetching the biking routes info")),
                        onCancellation = null)
                  }
                }

                override fun onFailure(call: Call<APIResponse<Relation>>, t: Throwable) {
                  continuation.resume(Response.Failure(t), onCancellation = null)
                }
              })
    }
  }

  override suspend fun getBikingRouteById(id: Long): Response<Relation> {
    return suspendCancellableCoroutine { continuation ->
      osmApiService
          .getBikingRouteById(bikingRouteQuery(id))
          .enqueue(
              object : Callback<APIResponse<Relation>> {
                override fun onResponse(
                    call: Call<APIResponse<Relation>>,
                    response: retrofit2.Response<APIResponse<Relation>>
                ) {
                  if (response.isSuccessful) {
                    continuation.resume(
                        Response.Success(response.body()!!.elements[0]), onCancellation = null)
                  } else {
                    continuation.resume(
                        Response.Failure(Exception("Error fetching the biking route info")),
                        onCancellation = null)
                  }
                }

                override fun onFailure(call: Call<APIResponse<Relation>>, t: Throwable) {
                  continuation.resume(Response.Failure(t), onCancellation = null)
                }
              })
    }
  }

  /**
   * This method creates the climbing info query to fetch the climbing points from the OSM API.
   *
   * @param range The range in meters to search for climbing points.
   * @param lat The latitude of the location to search for climbing points.
   * @param lon The longitude of the location to search for climbing points.
   * @return The climbing info query as a [String].
   */
  private fun climbingInfoQuery(range: Int, lat: Double, lon: Double): String {
    return "[out:json][timeout:25];\n" +
        "(\n" +
        "  node(around: $range, $lat, $lon)[sport=climbing][name];\n" +
        "  way(around: $range, $lat, $lon)[sport=climbing][name];\n" +
        ");\n" +
        "out tags;\n"
  }

  /**
   * This method creates the climbing point query to fetch a climbing point by its id from the OSM
   * API.
   *
   * @param id The OSM id of the climbing point to fetch.
   * @return The climbing point query as a [String].
   */
  private fun climbingPointQuery(id: Long): String {
    return "[out:json][timeout:25];\n(\n  nw($id);\n);\nout center;\n"
  }

  /**
   * This method creates the hiking info query to fetch the hiking routes from the OSM API.
   *
   * @param range The range in meters to search for hiking routes.
   * @param lat The latitude of the location to search for hiking routes.
   * @param lon The longitude of the location to search for hiking routes.
   * @return The hiking info query as a [String].
   */
  private fun hikingInfoQuery(range: Int, lat: Double, lon: Double): String {
    return "[out:json][timeout:25];\n" +
        "(\n" +
        "\trel(around: $range, $lat, $lon)[route=hiking][name][from][to];\n" +
        ");\n" +
        "out tags;\n"
  }

  /**
   * This method creates the hiking route query to fetch a hiking route by its id from the OSM API.
   *
   * @param id The OSM id of the hiking route to fetch.
   * @return The hiking route query as a [String].
   */
  private fun hikingRouteQuery(id: Long): String {
    return "[out:json][timeout:25];\n(\n  rel($id);\n);\nout geom;\n"
  }

  /**
   * This method creates the biking info query to fetch the biking routes from the OSM API.
   *
   * @param range The range in meters to search for biking routes.
   * @param lat The latitude of the location to search for biking routes.
   * @param lon The longitude of the location to search for biking routes.
   * @return The biking info query as a [String].
   */
  private fun bikingInfoQuery(range: Int, lat: Double, lon: Double): String {
    return "[out:json][timeout:25];\n" +
        "(\n" +
        "  rel(around: $range, $lat, $lon)[route=bicycle][from][to][distance];\n" +
        ");\n" +
        "out tags;\n"
  }

  /**
   * This method creates the biking route query to fetch a biking route by its id from the OSM API.
   *
   * @param id The OSM id of the biking route to fetch.
   * @return The biking route query as a [String].
   */
  private fun bikingRouteQuery(id: Long): String {
    return "[out:json][timeout:25];\n(\n  rel($id);\n);\nout geom;\n"
  }
}
