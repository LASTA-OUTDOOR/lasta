package com.lastaoutdoor.lasta.data.api

import com.lastaoutdoor.lasta.data.model.api.NodeWay
import com.lastaoutdoor.lasta.data.model.api.Relation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// interface used to formulate specific API calls
interface ApiService {

  @GET("interpreter")
  fun getClimbingPointsInfo(@Query("data") query: String): Call<APIResponse<NodeWay>>

  @GET("interpreter")
  fun getClimbingPointById(@Query("data") query: String): Call<APIResponse<NodeWay>>

  @GET("interpreter")
  fun getHikingRoutesInfo(@Query("data") query: String): Call<APIResponse<Relation>>
  @GET("interpreter")
  fun getHikingRouteById(@Query("data") query: String): Call<APIResponse<Relation>>

  @GET("interpreter")
  fun getBikingRoutesInfo(@Query("data") query: String): Call<APIResponse<Relation>>
  @GET("interpreter")
  fun getBikingRouteById(@Query("data") query: String): Call<APIResponse<Relation>>
}
