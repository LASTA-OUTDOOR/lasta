package com.lastaoutdoor.lasta.data.api.autocomplete

import com.lastaoutdoor.lasta.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 * This is an interface defining the different endpoints of the Radar API. We use Retrofit annotations
 * to define the HTTP requests.
 */
interface RadarApiService {
    @Headers("Authorization: " + BuildConfig.RADAR_API_KEY)
    @GET("autocomplete")
    fun getSuggestions(@Query("query") query: String, @Query("layers") layers : String, @Query("country") country : String = "CH", @Query("limit") limit : Int = 10) : Call<RadarApiResponse>

}