package com.lastaoutdoor.lasta.model.api

import com.lastaoutdoor.lasta.model.data.Node
import retrofit2.Call
import retrofit2.http.GET

// interface used to formulate specific API calls
interface ApiService {
  @GET(
      "interpreter?data=[out:json];area(id:3600051701)->.searchArea;(node[sport=climbing](area.searchArea););out%20geom;")
  fun getClimbingActivity(): Call<OutdoorActivityResponse<Node>>
}
