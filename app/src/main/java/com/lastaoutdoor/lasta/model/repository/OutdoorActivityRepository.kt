package com.lastaoutdoor.lasta.model.repository

import com.lastaoutdoor.lasta.model.api.ApiService
import com.lastaoutdoor.lasta.model.api.OutdoorActivityResponse
import com.lastaoutdoor.lasta.model.data.Node
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
  suspend fun getClimbingActivities(): OutdoorActivityResponse<Node> {

    val call = apiService.getClimbingActivity()
    val pr = call.execute()
    return OutdoorActivityResponse(pr.body()!!.version, (pr.body()!!.elements))
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
  val q = f.getClimbingActivities()
  println(q.elements.toString())
}
