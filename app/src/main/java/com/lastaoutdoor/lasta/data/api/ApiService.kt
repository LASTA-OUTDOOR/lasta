package com.lastaoutdoor.lasta.model.api

import com.lastaoutdoor.lasta.model.data.Node
import com.lastaoutdoor.lasta.model.data.Relation
import com.lastaoutdoor.lasta.model.data.Way
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// interface used to formulate specific API calls
interface ApiService {
  // 3 different functions since retrofit doesn't allow generic parameters in API Call parameters
  @GET("interpreter") fun getNode(@Query("data") data: String): Call<OutdoorActivityResponse<Node>>

  @GET("interpreter") fun getWay(@Query("data") data: String): Call<OutdoorActivityResponse<Way>>

  @GET("interpreter")
  fun getRelation(@Query("data") data: String): Call<OutdoorActivityResponse<Relation>>
}
