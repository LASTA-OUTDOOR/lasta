package com.lastaoutdoor.lasta.data.api

import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.data.model.api.Relation
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

    override suspend fun getHikingRoutesInfo(range: Int, lat: Double, lon: Double): Response<List<Relation>> {
        return suspendCancellableCoroutine { continuation ->
            apiService.getHikingRoutesInfo(hikingInfoQuery(range, lat, lon)).enqueue(object : Callback<APIResponse<Relation>> {
                override fun onResponse(
                    call: Call<APIResponse<Relation>>,
                    response: retrofit2.Response<APIResponse<Relation>>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(Response.Success(response.body()!!.elements), onCancellation = null)
                    } else {
                        continuation.resume(Response.Failure(Exception("Error fetching the hiking routes info")), onCancellation = null)
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
            apiService.getHikingRouteById(hikingRouteQuery(id)).enqueue(object : Callback<APIResponse<Relation>> {
                override fun onResponse(
                    call: Call<APIResponse<Relation>>,
                    response: retrofit2.Response<APIResponse<Relation>>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(Response.Success(response.body()!!.elements[0]), onCancellation = null)
                    } else {
                        continuation.resume(Response.Failure(Exception("Error fetching the hiking route info")), onCancellation = null)
                    }
                }

                override fun onFailure(call: Call<APIResponse<Relation>>, t: Throwable) {
                    continuation.resume(Response.Failure(t), onCancellation = null)
                }
            })
        }
    }

    override suspend fun getBikingRoutesInfo(range: Int, lat: Double, lon: Double): Response<List<Relation>> {
        return suspendCancellableCoroutine { continuation ->
            apiService.getBikingRoutesInfo(bikingInfoQuery(range, lat, lon)).enqueue(object : Callback<APIResponse<Relation>> {
                override fun onResponse(
                    call: Call<APIResponse<Relation>>,
                    response: retrofit2.Response<APIResponse<Relation>>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(Response.Success(response.body()!!.elements), onCancellation = null)
                    } else {
                        continuation.resume(Response.Failure(Exception("Error fetching the biking routes info")), onCancellation = null)
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
            apiService.getBikingRouteById(bikingRouteQuery(id)).enqueue(object : Callback<APIResponse<Relation>> {
                override fun onResponse(
                    call: Call<APIResponse<Relation>>,
                    response: retrofit2.Response<APIResponse<Relation>>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(Response.Success(response.body()!!.elements[0]), onCancellation = null)
                    } else {
                        continuation.resume(Response.Failure(Exception("Error fetching the biking route info")), onCancellation = null)
                    }
                }

                override fun onFailure(call: Call<APIResponse<Relation>>, t: Throwable) {
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

    private fun hikingInfoQuery(range: Int, lat: Double, lon: Double): String {
        return "[out:json][timeout:25];\n" +
            "(\n" +
            "\trel(around: $range, $lat, $lon)[route=hiking][name][from][to];\n" +
            ");\n" +
            "out tags;\n"
    }

    private fun hikingRouteQuery(id: Long): String {
        return "[out:json][timeout:25];\n" +
            "(\n" +
            "  rel($id);\n" +
            ");\n" +
            "out geom;\n"
    }

    private fun bikingInfoQuery(range: Int, lat: Double, lon: Double): String {
        return "[out:json][timeout:25];\n" +
            "(\n" +
            "  rel(around: $range, $lat, $lon)[route=bicycle][from][to][distance];\n" +
            ");\n" +
            "out tags;\n"
    }

    private fun bikingRouteQuery(id: Long): String {
        return "[out:json][timeout:25];\n" +
            "(\n" +
            "  rel($id);\n" +
            ");\n" +
            "out geom;\n"
    }
}
