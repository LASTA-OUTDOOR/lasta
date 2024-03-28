package com.lastaoutdoor.lasta.model.api
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("interpreter?data=[out:json];area(id:3600051701)->.searchArea;(node[sport=climbing](area.searchArea););out%20geom;")
    fun getClimbingActivity(): Call<OutdoorActivityResponse>

}