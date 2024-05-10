package com.lastaoutdoor.lasta.data.api.autocomplete

import com.lastaoutdoor.lasta.models.api.RadarSuggestion
import com.lastaoutdoor.lasta.repository.api.RadarRepository
import com.lastaoutdoor.lasta.utils.Response
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback

class RadarRepositoryImpl @Inject constructor(private val radarApiService: RadarApiService) :
    RadarRepository {

  private val layers: String = "county,locality,state"

  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun getSuggestions(query: String): Response<List<RadarSuggestion>> {
    return suspendCancellableCoroutine { continuation ->
      radarApiService
          .getSuggestions(query, layers)
          .enqueue(
              object : Callback<RadarApiResponse> {

                override fun onResponse(
                    call: Call<RadarApiResponse>,
                    response: retrofit2.Response<RadarApiResponse>
                ) {
                  if (response.isSuccessful) {
                    continuation.resume(
                        Response.Success(response.body()!!.suggestions), onCancellation = null)
                  } else {
                    // print error message
                    continuation.resume(
                        Response.Failure(Exception("Error fetching the suggestions")),
                        onCancellation = null)
                  }
                }

                override fun onFailure(call: Call<RadarApiResponse>, t: Throwable) {
                  continuation.resume(Response.Failure(t), onCancellation = null)
                }
              })
    }
  }
}
