package com.lastaoutdoor.lasta.model.repository
import android.telecom.Call
import android.util.Log
import com.lastaoutdoor.lasta.model.api.ApiResponse
import com.lastaoutdoor.lasta.model.api.ApiService
import com.lastaoutdoor.lasta.model.api.OutdoorActivityResponse
import com.lastaoutdoor.lasta.model.data.Node
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class OutdoorActivityRepository(/*context: Context*/) {
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://overpass-api.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    suspend fun getClimbingActivities(): OutdoorActivityResponse{

        val call = apiService.getClimbingActivity()
        var falseCall = false

        val pr = call.execute()

        return OutdoorActivityResponse(pr.body()!!.version, pr.body()!!.elements)
    }
    /**private val database = OutdoorActivityDatabase.getInstance(context)
    private val outdoorActivityDao = database.outdoorActivityDao()
    @@ -27,4 +69,9 @@ class OutdoorActivityRepository(context: Context) {
    suspend fun delete(outdoorActivity: OutdoorActivity) {
    outdoorActivityDao.delete(outdoorActivity)
    } */
}
suspend fun main(){
    val f = OutdoorActivityRepository()
    val q = f.getClimbingActivities()
    println(q.elements.toString())
}