package com.lastaoutdoor.lasta.data.api.autocomplete

import com.lastaoutdoor.lasta.BuildConfig
import com.lastaoutdoor.lasta.data.api.weather.Main
import com.lastaoutdoor.lasta.data.api.weather.WeatherApiService
import com.lastaoutdoor.lasta.data.api.weather.WeatherRepositoryImpl
import com.lastaoutdoor.lasta.data.api.weather.WeatherResponse
import com.lastaoutdoor.lasta.data.api.weather.Wind
import com.lastaoutdoor.lasta.repository.api.RadarRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Call

class FakeRadarApiServiceTest : RadarApiService {
    override fun getSuggestions(query: String, layers: String, country: String, limit: Int): Call<RadarApiResponse> {
        return mock(Call::class.java) as Call<RadarApiResponse>
    }

}

class RadarRepositoryImplTest {

}
