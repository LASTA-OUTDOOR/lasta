package com.lastaoutdoor.lasta.data.api.autocomplete

import org.junit.Assert.*
import org.mockito.Mockito.mock
import retrofit2.Call

class FakeRadarApiServiceTest : RadarApiService {
  override fun getSuggestions(
      query: String,
      layers: String,
      country: String,
      limit: Int
  ): Call<RadarApiResponse> {
    return mock(Call::class.java) as Call<RadarApiResponse>
  }
}

class RadarRepositoryImplTest {}
