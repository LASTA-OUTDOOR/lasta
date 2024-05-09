package com.lastaoutdoor.lasta.data.api.autocomplete

import com.lastaoutdoor.lasta.models.api.RadarSuggestion
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class RadarApiResponseTest {

    private lateinit var radarApiResponse: RadarApiResponse

    @Before
    fun setUp() {
        radarApiResponse = RadarApiResponse(emptyList())
    }

    @Test
    fun getSuggestions() {
        assert(radarApiResponse.suggestions.isEmpty())
        val suggestions = listOf(RadarSuggestion("suggestion", 80.0, 80.0))
        radarApiResponse = RadarApiResponse(suggestions)
        assert(radarApiResponse.suggestions.isNotEmpty())
    }

    @Test
    fun copy() {
        val suggestions = listOf(RadarSuggestion("suggestion", 80.0, 80.0))
        radarApiResponse = RadarApiResponse(suggestions)
        val copy = radarApiResponse.copy()
        assert(copy == radarApiResponse)
    }

}